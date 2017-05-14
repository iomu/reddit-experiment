package de.iomu.reddit_experiment.features.subreddit

import dagger.android.AndroidInjector
import com.bluelinelabs.conductor.Controller
import dagger.*
import de.iomu.reddit_experiment.base.ControllerKey
import dagger.multibindings.IntoMap
import de.iomu.reddit_experiment.base.ControllerScope
import de.iomu.reddit_experiment.features.link.LinkController
import javax.inject.Named

@ControllerScope
@Subcomponent(modules = arrayOf(LinkModule::class))
interface LinkComponent : AndroidInjector<LinkController> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<LinkController>() {
        abstract fun linkModule(subredditModule: LinkModule): Builder
        override fun seedInstance(instance: LinkController) {
            linkModule(LinkModule(instance))
        }
    }
}

@Module(subcomponents = arrayOf(LinkComponent::class))
abstract class LinkInjectionModule {
    @Binds
    @IntoMap
    @ControllerKey(LinkController::class)
    internal abstract fun bindLinkControllerInjectorFactory(builder: LinkComponent.Builder): AndroidInjector.Factory<out Controller>
}

@Module
class LinkModule(val controller: LinkController) {
    @Provides
    @ControllerScope
    @Named("link_id")
    fun provideLinkId(): String {
        return controller.linkId
    }
}
