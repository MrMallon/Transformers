package mr.fugugames.com.transformers.viewTransformers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mr.fugugames.com.transformers.models.BattleResult
import mr.fugugames.com.transformers.models.Transformers
import mr.fugugames.com.transformers.war.Battle

class ViewTransformersViewModel(private val repo: ViewTransformersRepo) : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val battle = Battle()

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String>
        get() = _successMessage

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _robotList = MutableLiveData<MutableList<Transformers>>()
    val robotListLiveData: LiveData<MutableList<Transformers>>
        get() = _robotList

    private var _result = MutableLiveData<BattleResult>()
    val resultLiveData: LiveData<BattleResult>
        get() = _result


    fun getTransformers(){
        //todo: call api for data and update db
        _loading.value = true
        ioScope.launch {
            val list = repo.getLocalTransformers()
            uiScope.launch { _robotList.value = list }
        }
        _loading.value = false
    }

    fun deleteTransformer(data:Transformers){
        _loading.value = true
        ioScope.launch {
            uiScope.launch {
                val result = repo.deleteTransformer(data.id)
                if (result != null) {
                    _robotList.value?.remove(data)
                    _successMessage.value = "Successfully to Deleted Transformer: ${data.name}"
                } else
                    _errorMessage.value = "Failed to Delete Transformer: ${data.name} "
            }
        }
        _loading.value = false
    }

    fun beginBattle() {
        if (_robotList.value == null || !_robotList.value!!.any()) {
            _errorMessage.value = "Please add Transformers by clicking the Plus button"
            return
        }
        _result.value = battle.startBattle(_robotList.value!!)
        ioScope.launch {cleanUpDeadBots()}
    }

    private fun cleanUpDeadBots(){
        ioScope.launch {
            val remaining = repo.cleanUpAfterWar(_result.value!!.bots)
            uiScope.launch {_robotList.value = remaining}
        }
    }
}
