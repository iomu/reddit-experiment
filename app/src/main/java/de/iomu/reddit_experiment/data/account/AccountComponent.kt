package de.iomu.reddit_experiment.data.account

import android.app.Activity
import dagger.Subcomponent
import dagger.android.DispatchingAndroidInjector
import de.iomu.reddit_experiment.MainActivityModule
import de.iomu.reddit_experiment.data.store.StoreModule
import javax.inject.Scope

@UserScope
@Subcomponent(modules = arrayOf(AccountModule::class, MainActivityModule::class, StoreModule::class))
interface AccountComponent {
    @Subcomponent.Builder
    interface Builder {
        fun accountModule(accountModule: AccountModule): Builder
        fun build(): AccountComponent
    }

    fun provideActivityInjector(): DispatchingAndroidInjector<Activity>
}

@Scope
annotation class UserScope