package mr.fugugames.com.transformers.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "transformers")
data class Transformers(
    @PrimaryKey
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("strength") val strength : Int,
    @SerializedName("intelligence") val intelligence : Int,
    @SerializedName("speed") val speed : Int,
    @SerializedName("endurance") val endurance : Int,
    @SerializedName("rank") val rank : Int,
    @SerializedName("courage") val courage : Int,
    @SerializedName("firepower") val firepower : Int,
    @SerializedName("skill") val skill : Int,
    @SerializedName("team") val team : String,
    @SerializedName("team_icon") val team_icon : String,
    var isAlive: Boolean = true,
    var hasRunAway: Boolean = false
)