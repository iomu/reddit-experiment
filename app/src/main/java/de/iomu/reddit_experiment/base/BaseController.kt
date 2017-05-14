package de.iomu.reddit_experiment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bluelinelabs.conductor.Controller
import timber.log.Timber

abstract class BaseController(args: Bundle?) : Controller() {
    private var unbinder: Unbinder? = null

    constructor() : this(null)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflate(inflater, container)
        unbinder = ButterKnife.bind(this, view)
        inject(this)
        onViewBound(view)
        return view
    }

    protected abstract fun inflate(inflater: LayoutInflater, container: ViewGroup): View

    open protected fun onViewBound(view: View) {}

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        unbinder?.unbind()
        unbinder = null
    }

    companion object {

        fun inject(controller: Controller) {
            val hasControllerInjector = findHasControllerInjector(controller) ?: return
            Timber.d("Injector provider was found: %s", hasControllerInjector::class.java.canonicalName)
            val controllerInjector = hasControllerInjector.controllerInjector()
            controllerInjector.inject(controller)
        }

        fun findHasControllerInjector(controller: Controller): HasDispatchingControllerInjector? {
            if (controller is HasDispatchingControllerInjector) {
                return controller
            }
            if (controller.activity is HasDispatchingControllerInjector) {
                return controller.activity as HasDispatchingControllerInjector
            }

            if (controller.activity?.application is HasDispatchingControllerInjector) {
                return controller.activity?.application as HasDispatchingControllerInjector
            }

            val parent = controller.parentController

            if (parent == null) {
                return null
            } else {
                return findHasControllerInjector(parent)
            }
        }
    }
}