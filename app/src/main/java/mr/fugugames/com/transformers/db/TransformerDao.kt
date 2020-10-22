package mr.fugugames.com.transformers.db

import androidx.room.*
import mr.fugugames.com.transformers.models.Transformers

@Dao
interface TransformerDao {
    @Query("SELECT * from transformers")
    fun getLocal() : MutableList<Transformers>

    @Query("DELETE from transformers")
    fun deleteAll()

    @Query("SELECT * from transformers where id == :id")
    fun getTransformerById(id: String): Transformers

    @Query("DELETE from transformers where id == :id")
    fun deleteById(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg transformer: Transformers)

    fun insertList(transformerList: List<Transformers>){
        for(x in transformerList){
            insert(x)
        }
    }
}