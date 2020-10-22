package mr.fugugames.com.transformers.viewTransformers.ui

import mr.fugugames.com.transformers.models.Transformers

class TransformerOnClickListener (val clickListener: (data: Transformers) -> Unit) {
    fun onClick(data: Transformers) = clickListener(data)
}