package mr.fugugames.com.transformers.models

import com.google.gson.annotations.SerializedName

data class TransformersData(
    @SerializedName("transformers")val transformers: MutableList<Transformers>
)