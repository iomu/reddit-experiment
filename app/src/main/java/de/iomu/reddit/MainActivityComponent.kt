package de.iomu.reddit

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import de.iomu.reddit.base.ConductorInjectionModule
import de.iomu.reddit.features.subreddit.SubredditInjectionModule

@Subcomponent(modules = arrayOf(ConductorInjectionModule::class, SubredditInjectionModule::class))
interface MainActivityComponent : AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}

@Module(subcomponents = arrayOf(MainActivityComponent::class))
abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    abstract fun bindMainActivityInjectorFactory(builder: MainActivityComponent.Builder): AndroidInjector.Factory<out Activity>
}