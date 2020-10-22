package mr.fugugames.com.transformers.createTransformers

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.create_transformer_fragment.*
import mr.fugugames.com.transformers.R
import mr.fugugames.com.transformers.createTransformers.CreateTransformerFragmentDirections.toViewTransformersFragment
import mr.fugugames.com.transformers.databinding.CreateTransformerFragmentBinding
import mr.fugugames.com.transformers.extensions.NumberInputFilter
import mr.fugugames.com.transformers.extensions.bindVM
import mr.fugugames.com.transformers.extensions.setTitle
import org.kodein.di.generic.instance

class CreateTransformerFragment : Fragment() {

    private val viewModel by bindVM {
        CreateTransformerViewModel(instance())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CreateTransformerFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUi()
        setFilters()
        viewModel.init()
    }

    override fun onResume() {
        super.onResume()
        this.setTitle("Create Transformer")
    }

    private fun bindUi(){
        team_button_group.setOnCheckedChangeListener { group, checkid ->
            if(R.id.autobot_rad == checkid) {
                team_img.setImageResource(R.drawable.ic_icon)
                viewModel.setTeam("A")
            }
            else {
                team_img.setImageResource(R.drawable.ic_decepticon)
                viewModel.setTeam("D")
            }
        }
        create_btn.setOnClickListener { viewModel.createTransformer() }

        setObservers()
        checkArguments()
    }

    private fun checkArguments(){
        arguments?.let {
            val safeArgs = CreateTransformerFragmentArgs.fromBundle(it)
            val id = safeArgs.id
            if(id.isNotEmpty()) {
                create_btn.text = "Update"
                viewModel.getLocalTransformer(id)
                return
            }
            create_btn.text = "Create"
            viewModel.isEditTransformer = false
        }
    }

    private fun setObservers(){
        viewModel.errorMessage.observe(this.viewLifecycleOwner, Observer{
            if(it.isEmpty())
                return@Observer
            displaySnackBar(it)
        })
        viewModel.successMessage.observe(this.viewLifecycleOwner, Observer{
            if(it.isEmpty())
                return@Observer
            displaySnackBar(it, Snackbar.LENGTH_SHORT)
            findNavController().navigate(toViewTransformersFragment())
        })

        viewModel.loading.observe(this.viewLifecycleOwner, Observer {
            if(it == true)
                progressBar.visibility = View.VISIBLE
            else
                progressBar.visibility = View.GONE
        })
        viewModel.team.observe(this.viewLifecycleOwner, Observer {
            if (it == "D")
                team_img.setImageResource(R.drawable.ic_decepticon)
            else if (it == "A")
                team_img.setImageResource(R.drawable.ic_icon)
        })
    }

    private fun setFilters(){
        intelligence.filters = arrayOf(NumberInputFilter())
        skill.filters = arrayOf(NumberInputFilter())
        strength.filters = arrayOf(NumberInputFilter())
        speed.filters = arrayOf(NumberInputFilter())
        endurance.filters = arrayOf(NumberInputFilter())
        courage.filters = arrayOf(NumberInputFilter())
        rank.filters = arrayOf(NumberInputFilter())
        firepower.filters = arrayOf(NumberInputFilter())
    }

    private  fun displaySnackBar(message:String, length: Int = Snackbar.LENGTH_LONG){
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            length
        ).show()
    }
}
