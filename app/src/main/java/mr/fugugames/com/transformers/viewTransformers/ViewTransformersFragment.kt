package mr.fugugames.com.transformers.viewTransformers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.create_transformer_fragment.*
import kotlinx.android.synthetic.main.view_transformers_fragment.*
import mr.fugugames.com.transformers.R
import mr.fugugames.com.transformers.createTransformers.CreateTransformerFragmentDirections
import mr.fugugames.com.transformers.extensions.bindVM
import mr.fugugames.com.transformers.extensions.goes
import mr.fugugames.com.transformers.extensions.setTitle
import mr.fugugames.com.transformers.models.Transformers
import mr.fugugames.com.transformers.viewTransformers.ViewTransformersFragmentDirections.toCreateTransformerFragment
import mr.fugugames.com.transformers.viewTransformers.ui.TransformerAdapter
import mr.fugugames.com.transformers.viewTransformers.ui.TransformerOnClickListener
import org.kodein.di.generic.instance

class ViewTransformersFragment : Fragment() {

    private lateinit var adapter: TransformerAdapter

    private val viewModel by bindVM {
        ViewTransformersViewModel(instance())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getTransformers()
        return inflater.inflate(R.layout.view_transformers_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUi()
    }

    override fun onResume() {
        super.onResume()
        this.setTitle("Transformers")
    }

    private fun bindUi(){
        new_transformer_btn goes toCreateTransformerFragment("")
        battle_btn.setOnClickListener { viewModel.beginBattle() }
        setupRecyclerView()
        setObservables()
    }

    private fun setObservables(){
        viewModel.errorMessage.observe(this.viewLifecycleOwner, Observer{
            if(it.isEmpty())
                return@Observer
            displaySnackBar(it)
        })

        viewModel.loading.observe(this.viewLifecycleOwner, Observer {
            if(it == true)
                progressBar2.visibility = View.VISIBLE
            else
                progressBar2.visibility = View.GONE
        })
        viewModel.successMessage.observe(this.viewLifecycleOwner, Observer{
            if(it.isEmpty())
                return@Observer
            displaySnackBar(it, Snackbar.LENGTH_SHORT)
        })
        viewModel.robotListLiveData.observe(this.viewLifecycleOwner, Observer { adapter.updateList(it) })

        viewModel.resultLiveData.observe(this.viewLifecycleOwner, Observer {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("It's done")
            builder.setMessage("The War is Over! ${it.winner} won!")

            builder.setPositiveButton("Bokay") { dialog, which ->
            }
            builder.show()
        })
    }

    private fun setupRecyclerView(){
        adapter = TransformerAdapter(TransformerOnClickListener { editTransformer((it)) }, TransformerOnClickListener { deleteTransformer(it) })
        val manager = LinearLayoutManager(this.activity)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
    }

    private fun editTransformer(transformers: Transformers){
        findNavController().navigate(toCreateTransformerFragment(transformers.id))
    }

    private fun deleteTransformer(transformers: Transformers){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Warning!!")
        builder.setMessage("Are you sure you want to delete ${transformers.name}?")

        builder.setPositiveButton("Yes") { dialog, which ->
            viewModel.deleteTransformer(transformers)
        }

        builder.setNegativeButton("No") { dialog, which ->

        }
        builder.show()
    }

    private fun displaySnackBar(message:String, length: Int = Snackbar.LENGTH_LONG){
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            length
        ).show()
    }
}
