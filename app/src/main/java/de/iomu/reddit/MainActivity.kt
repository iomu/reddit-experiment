package de.iomu.reddit


import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.nytimes.android.external.store2.base.impl.BarCode
import com.nytimes.android.external.store2.base.impl.Store
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import de.iomu.reddit.base.HasDispatchingControllerInjector
import de.iomu.reddit.data.account.Account
import de.iomu.reddit.data.api.RedditApi
import de.iomu.reddit.data.model.CommentResponse
import de.iomu.reddit.data.model.Link
import de.iomu.reddit.data.model.Listing
import de.iomu.reddit.data.store.LinkKey
import de.iomu.reddit.data.store.Subreddit
import de.iomu.reddit.features.subreddit.SubredditController
import timber.log.Timber
import javax.inject.Inject

class MainActivity : Activity(), HasDispatchingControllerInjector {
    @Inject
    lateinit var api: RedditApi

    @Inject
    lateinit var store: Store<Listing<Link>, Subreddit>

    @Inject
    lateinit var controllerInjector: DispatchingAndroidInjector<Controller>

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        setContentView(R.layout.activity_main)
        router = setupRouter(savedInstanceState)
    }

    private fun setupRouter(savedInstanceState: Bundle?): Router {
        val container = findViewById(R.id.container) as ViewGroup

        val router = Conductor.attachRouter(this, container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(SubredditController("android")))
        }
        return router
    }

    override fun controllerInjector(): DispatchingAndroidInjector<Controller> {
        return controllerInjector
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
