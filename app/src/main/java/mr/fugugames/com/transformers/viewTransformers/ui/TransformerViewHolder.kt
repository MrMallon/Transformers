package mr.fugugames.com.transformers.viewTransformers.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mr.fugugames.com.transformers.databinding.TransformerViewHolderBinding
import mr.fugugames.com.transformers.models.Transformers

class TransformerViewHolder private constructor(private val binding: TransformerViewHolderBinding):  RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): TransformerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TransformerViewHolderBinding.inflate(layoutInflater, parent, false)

            return TransformerViewHolder(binding)
        }
    }
    fun bind(item: Transformers, editClickListener: TransformerOnClickListener, deleteClickListener: TransformerOnClickListener){
        binding.data = item
        binding.editClicked = editClickListener
        binding.deleteClicked = deleteClickListener
    }
}