package de.iomu.reddit_experiment.app

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Module
class AppModule(private val app: RedditApp) {
    @Provides
    fun provideApplication(): RedditApp {
        return app
    }

    @Provides
    @App
    fun provideApplicationContext(): Context {
        return app
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class App