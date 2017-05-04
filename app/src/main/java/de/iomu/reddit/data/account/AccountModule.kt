package de.iomu.reddit.data.account

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AccountModule(val account: Account) {
    @Provides
    @UserScope
    fun provideAccount(): Account {
        return account
    }
}