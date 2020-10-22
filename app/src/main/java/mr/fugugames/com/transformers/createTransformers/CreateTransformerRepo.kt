package mr.fugugames.com.transformers.createTransformers

import android.util.Log
import mr.fugugames.com.transformers.db.TransformerDao
import mr.fugugames.com.transformers.httpService.TransformersService
import mr.fugugames.com.transformers.models.Transformers
import mr.fugugames.com.transformers.models.TransformersData
import java.lang.Exception

interface CreateTransformerRepo {
    suspend fun createTransformer(transformer: Transformers):Transformers?
    suspend fun updateTransformer(transformer: Transformers):Transformers?
    suspend fun getTransformers(): TransformersData
    fun getLocalTransformer(id:String): Transformers
}

class CreateTransformerRepoImpl(private val apiService: TransformersService, private val dao: TransformerDao): CreateTransformerRepo{
    override suspend fun createTransformer(transformer: Transformers):Transformers? {
        return try {
            val data = apiService.createNewTransformerAsync(transformer).await()
            data.isAlive = true
            storeTransformer(data)
            data
        } catch (e: Exception){
            Log.d("createTransformer", e.message!!)
            null
        }
    }

    override suspend fun updateTransformer(transformer: Transformers): Transformers? {
        return try {
            val data = apiService.updateTransformerAsync(transformer).await()
            storeTransformer(data)
            data
        } catch (e: Exception){
            Log.d("updateTransformer", e.message!!)
            null
        }
    }

    override suspend fun getTransformers(): TransformersData {
        return try {
            apiService.getTransformersAsync().await()
        } catch (e: Exception){
            Log.d("getTransformers", e.message!!)
            TransformersData(transformers = mutableListOf())
        }
    }

    override fun getLocalTransformer(id:String): Transformers {
        return dao.getTransformerById(id)
    }

    private fun storeTransformer(data: Transformers){
        dao.insert(data)
    }

}