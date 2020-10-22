package mr.fugugames.com.transformers.viewTransformers.ui

import androidx.recyclerview.widget.DiffUtil
import mr.fugugames.com.transformers.models.Transformers

class TransformersDiffCallback : DiffUtil.ItemCallback<Transformers>() {
    override fun areItemsTheSame(oldItem: Transformers, newItem: Transformers): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transformers, newItem: Transformers): Boolean {
        return oldItem == newItem
    }
}