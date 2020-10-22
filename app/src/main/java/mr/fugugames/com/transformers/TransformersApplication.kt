package mr.fugugames.com.transformers

import android.app.Application
import kodeinViewModelInjector.KodeinViewModelInjector
import mr.fugugames.com.transformers.createTransformers.CreateTransformerRepo
import mr.fugugames.com.transformers.createTransformers.CreateTransformerRepoImpl
import mr.fugugames.com.transformers.db.TransformerDao
import mr.fugugames.com.transformers.db.TransformerDb
import mr.fugugames.com.transformers.httpService.TransformersService
import mr.fugugames.com.transformers.httpService.auth.AuthService
import mr.fugugames.com.transformers.httpService.auth.TokenHandler
import mr.fugugames.com.transformers.httpService.auth.TokenHandlerImpl
import mr.fugugames.com.transformers.viewTransformers.ViewTransformersRepo
import mr.fugugames.com.transformers.viewTransformers.ViewTransformersRepoImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class TransformersApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@TransformersApplication))
        bind<TokenHandler>() with provider { TokenHandlerImpl(applicationContext,instance()) }
        bind() from singleton { AuthService() }
        bind() from singleton { TransformersService(instance()) }
        bind() from singleton { TransformerDb(instance()) }
        bind() from singleton { instance<TransformerDb>().transformerDao() }
        bind<CreateTransformerRepo>() with provider { CreateTransformerRepoImpl(instance(), instance()) }
        bind<ViewTransformersRepo>() with provider { ViewTransformersRepoImpl(instance(), instance()) }
    }
    override fun onCreate() {
        super.onCreate()

        KodeinViewModelInjector.setContainerProvider { kodein }
    }
}