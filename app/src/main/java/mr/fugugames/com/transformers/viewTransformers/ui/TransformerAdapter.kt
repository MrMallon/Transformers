package mr.fugugames.com.transformers.viewTransformers.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mr.fugugames.com.transformers.models.Transformers

class TransformerAdapter(val editClickListener: TransformerOnClickListener, val deleteClickListener: TransformerOnClickListener) : ListAdapter<Transformers, RecyclerView.ViewHolder>(TransformersDiffCallback()){
    private val adapterScope = CoroutineScope(Dispatchers.Default)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TransformerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TransformerViewHolder -> {
                val data = getItem(position) as Transformers
                holder.bind(data, editClickListener, deleteClickListener)
            }
        }
    }

    fun updateList(list: MutableList<Transformers>){
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }
}