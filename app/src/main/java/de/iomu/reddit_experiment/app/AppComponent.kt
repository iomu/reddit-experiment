package de.iomu.reddit_experiment.app

import dagger.Component
import de.iomu.reddit_experiment.data.NetworkModule
import de.iomu.reddit_experiment.data.account.AccountComponent
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class))
interface AppComponent {
    fun inject(app: RedditApp)

    fun accountComponentBuilder(): AccountComponent.Builder
}