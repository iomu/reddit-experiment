package de.iomu.reddit.features.subreddit

import dagger.Component
import dagger.Subcomponent
import dagger.android.AndroidInjector
import com.bluelinelabs.conductor.Controller
import de.iomu.reddit.base.ControllerKey
import dagger.multibindings.IntoMap
import dagger.Binds
import dagger.Module
import de.iomu.reddit.base.ControllerScope

@ControllerScope
@Subcomponent
interface SubredditComponent : AndroidInjector<SubredditController> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<SubredditController>()
}

@Module(subcomponents = arrayOf(SubredditComponent::class))
abstract class SubredditModule {
    @Binds
    @IntoMap
    @ControllerKey(SubredditController::class)
    internal abstract fun bindYourControllerInjectorFactory(builder: SubredditComponent.Builder): AndroidInjector.Factory<out Controller>
}
