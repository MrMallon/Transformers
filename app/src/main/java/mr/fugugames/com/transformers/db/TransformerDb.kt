package mr.fugugames.com.transformers.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mr.fugugames.com.transformers.models.Transformers

@Database(
    entities = [Transformers::class],
    version = 1
)

abstract class TransformerDb :RoomDatabase(){
    abstract fun transformerDao():TransformerDao
    companion object {
        @Volatile
        private var instance: TransformerDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDb(context).also { instance = it }
        }

        private fun buildDb(context: Context) =
            Room.databaseBuilder(context.applicationContext, TransformerDb::class.java, "transformer_db.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}