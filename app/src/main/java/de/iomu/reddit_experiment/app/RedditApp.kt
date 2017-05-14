package de.iomu.reddit_experiment.app

import android.app.Activity
import android.app.Application
import com.facebook.soloader.SoLoader
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import de.iomu.reddit_experiment.BuildConfig
import de.iomu.reddit_experiment.data.account.AccountComponent
import de.iomu.reddit_experiment.data.account.AccountManager
import de.iomu.reddit_experiment.data.account.AccountModule
import timber.log.Timber
import javax.inject.Inject

class RedditApp : Application(), HasActivityInjector {
    lateinit private var appComponent: AppComponent

    @Inject
    lateinit var accountManager: AccountManager
    @Inject
    lateinit var accountComponentBuilder: AccountComponent.Builder

    lateinit var accountComponent: AccountComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        appComponent.inject(this)

        SoLoader.init(this, false)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        accountManager.currentAccount
                .subscribe {
                    accountComponent = accountComponentBuilder
                            .accountModule(AccountModule(it))
                            .build()
                }

    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return accountComponent.provideActivityInjector()
    }
}