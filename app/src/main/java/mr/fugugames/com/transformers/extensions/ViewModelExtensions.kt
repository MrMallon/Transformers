package mr.fugugames.com.transformers.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kodeinViewModelInjector.KodeinViewModelInjector
import org.kodein.di.Kodein
import org.kodein.di.bindings.NoArgBindingKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

inline fun <reified VM : ViewModel> Fragment.bindVM(noinline creator: NoArgBindingKodein<Any?>.() -> VM) =
    viewModelBinder<VM> {
        bind() from provider(creator)
    }

inline fun <reified VM : ViewModel> Fragment.viewModelBinder(
    baseContainer: Kodein = KodeinViewModelInjector.container,
    crossinline binder: (Kodein.Builder.() -> Unit)
) = lazy {
    ViewModelProvider(this, object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val testViewModel = KodeinViewModelInjector
                .getTestViewModel(VM::class.java)
            @Suppress("UNCHECKED_CAST")
            return when (testViewModel) {
                null ->
                    Kodein {
                        extend(baseContainer)
                        binder.invoke(this)
                    }.run {
                        val viewModel by instance<VM>()
                        viewModel
                    }
                else -> testViewModel
            } as T
        }
    })
        .get(VM::class.java)
}