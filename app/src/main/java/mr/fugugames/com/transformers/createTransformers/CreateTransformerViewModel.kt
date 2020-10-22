package mr.fugugames.com.transformers.createTransformers

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mr.fugugames.com.transformers.models.Transformers

class CreateTransformerViewModel(private val repo: CreateTransformerRepo) : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    fun init(){
        _loading.value = false
        name.set("")
        strength.set("1")
        intelligence.set("1")
        speed.set("1")
        endurance.set("1")
        rank.set("1")
        courage.set("1")
        firepower.set("1")
        skill.set("1")
        _team.value = ""
    }

    var id = ""
    var teamIcon = ""
    val name = ObservableField<String>()
    val strength = ObservableField<String>()
    val intelligence = ObservableField<String>()
    val speed = ObservableField<String>()
    val endurance = ObservableField<String>()
    val rank = ObservableField<String>()
    val courage = ObservableField<String>()
    val firepower = ObservableField<String>()
    val skill = ObservableField<String>()

    var isEditTransformer: Boolean = false

    private var  _team = MutableLiveData<String>()
    val team: LiveData<String>
        get() = _team

    private var _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String>
        get() = _successMessage

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun setTeam(teamSelected: String){
        _team.value = teamSelected
    }

    fun getLocalTransformer(id:String){
        this.id = id
        isEditTransformer = true
        ioScope.launch {
            val transformer = repo.getLocalTransformer(id)
            uiScope.launch {
                name.set(transformer.name)
                strength.set(transformer.strength.toString())
                intelligence.set(transformer.intelligence.toString())
                speed.set(transformer.speed.toString())
                endurance.set(transformer.endurance.toString())
                rank.set(transformer.rank.toString())
                courage.set(transformer.courage.toString())
                firepower.set(transformer.firepower.toString())
                skill.set(transformer.skill.toString())
                _team.value = transformer.team
                teamIcon = transformer.team_icon
            }
        }
    }

    fun createTransformer(){

        if(_loading.value!!)
            return

        if(name.get().isNullOrEmpty()){
            _errorMessage.value = "A Name is required"
            return
        }

        if(team.value.isNullOrEmpty()){
            _errorMessage.value = "A Team is required"
            return
        }

        if(isEditTransformer){
            updateTransformer()
            return
        }

        _loading.value = true
        ioScope.launch {
            val transformer = Transformers(
                id = "0",
                name = name.get()!!,
                team = _team.value!!,
                courage = courage.get()!!.toInt(),
                endurance = endurance.get()!!.toInt(),
                firepower = firepower.get()!!.toInt(),
                intelligence = intelligence.get()!!.toInt(),
                rank = rank.get()!!.toInt(),
                skill = skill.get()!!.toInt(),
                speed = speed.get()!!.toInt(),
                strength = strength.get()!!.toInt(),
                team_icon = ""
            )

            val details = repo.createTransformer(transformer)
            uiScope.launch {
                _loading.value = false
                if (details == null)
                    _errorMessage.value = "Failed to create Transformer: ${name.get()}"
                else
                    _successMessage.value = "Successfully to create Transformer: ${name.get()}"
            }
        }
    }

    private fun updateTransformer(){
        _loading.value = true
        ioScope.launch {
            val transformer = Transformers(
                id = id,
                name = name.get()!!,
                team = _team.value!!,
                courage = courage.get()!!.toInt(),
                endurance = endurance.get()!!.toInt(),
                firepower = firepower.get()!!.toInt(),
                intelligence = intelligence.get()!!.toInt(),
                rank = rank.get()!!.toInt(),
                skill = skill.get()!!.toInt(),
                speed = speed.get()!!.toInt(),
                strength = strength.get()!!.toInt(),
                team_icon = teamIcon
            )
            val details = repo.updateTransformer(transformer)

            uiScope.launch {
                if(details != null)
                    _successMessage.value = "Successfully to updated Transformer: ${name.get()}"
                else
                    _errorMessage.value = "Something unexpected happened, failed to update: ${name.get()}"
            }
        }
        _loading.value = false
    }
}
