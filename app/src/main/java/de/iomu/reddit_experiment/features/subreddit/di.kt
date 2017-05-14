package de.iomu.reddit_experiment.features.subreddit

import dagger.android.AndroidInjector
import com.bluelinelabs.conductor.Controller
import dagger.*
import de.iomu.reddit_experiment.base.ControllerKey
import dagger.multibindings.IntoMap
import de.iomu.reddit_experiment.base.ControllerScope
import javax.inject.Named

@ControllerScope
@Subcomponent(modules = arrayOf(SubredditModule::class))
interface SubredditComponent : AndroidInjector<SubredditController> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<SubredditController>() {
        abstract fun subredditModule(subredditModule: SubredditModule): Builder
        override fun seedInstance(instance: SubredditController) {
            subredditModule(SubredditModule(instance))
        }
    }
}

@Module(subcomponents = arrayOf(SubredditComponent::class))
abstract class SubredditInjectionModule {
    @Binds
    @IntoMap
    @ControllerKey(SubredditController::class)
    internal abstract fun bindSubredditControllerInjectorFactory(builder: SubredditComponent.Builder): AndroidInjector.Factory<out Controller>
}

@Module
class SubredditModule(val controller: SubredditController) {
    @Provides
    @ControllerScope
    @Named("subreddit")
    fun provideSubreddit(): String {
        return controller.subreddit
    }
}
