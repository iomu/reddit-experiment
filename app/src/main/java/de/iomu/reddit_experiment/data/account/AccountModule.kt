package de.iomu.reddit_experiment.data.account

import dagger.Module
import dagger.Provides

@Module
class AccountModule(val account: Account) {
    @Provides
    @UserScope
    fun provideAccount(): Account {
        return account
    }
}