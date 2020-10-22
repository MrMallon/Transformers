package mr.fugugames.com.transformers.viewTransformers

import android.util.Log
import mr.fugugames.com.transformers.db.TransformerDao
import mr.fugugames.com.transformers.httpService.TransformersService
import mr.fugugames.com.transformers.models.Transformers
import java.lang.Exception

interface ViewTransformersRepo {
    fun getLocalTransformers(): MutableList<Transformers>
    suspend fun cleanUpAfterWar(transformers: MutableList<Transformers>): MutableList<Transformers>
    suspend fun deleteTransformer(id:String): MutableList<Transformers>?
}

class ViewTransformersRepoImpl(private val dao: TransformerDao, private val apiService: TransformersService):ViewTransformersRepo{
    override fun getLocalTransformers(): MutableList<Transformers> {
        return dao.getLocal()
    }

    override suspend fun cleanUpAfterWar(transformers: MutableList<Transformers>): MutableList<Transformers> {
        return try {
            for (x in transformers.filter { !it.isAlive }){
                //apiService.deleteTransformerById(x.id).await() todo fix api giving 401 error?
                dao.deleteById(x.id)
            }
            dao.getLocal()
        } catch (e: Exception){
            Log.d("cleanUpAfterWar", e.message!!)
            transformers
        }
    }

    override suspend fun deleteTransformer(id: String): MutableList<Transformers>?{
        return try {
            val newList = apiService.deleteTransformerById(id).await()
            dao.deleteById(id)

            newList.transformers
        } catch (e: Exception){
            Log.d("deleteTransformer", e.message!!)
            null
        }
    }

}