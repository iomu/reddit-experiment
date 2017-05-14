package de.iomu.reddit_experiment.data.account

import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManager @Inject constructor() {
    val currentAccount: Observable<Account>
        get() {
            return Observable.just(Account.LoggedOut)
        }
}