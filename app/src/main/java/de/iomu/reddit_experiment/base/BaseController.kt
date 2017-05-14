package de.iomu.reddit_experiment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import timber.log.Timber

abstract class BaseController(args: Bundle?) : Controller() {
    private var injected: Boolean = false
    init {
        addLifecycleListener(object : LifecycleListener() {
            override fun postCreateView(controller: Controller, view: View) {
                onViewBound(view)
            }
        })
    }
    constructor() : this(null)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflate(inflater, container)
        if (!injected) {
            inject(this)
            injected = true
        }

        return view
    }

    protected abstract fun inflate(inflater: LayoutInflater, container: ViewGroup): View

    open protected fun onViewBound(view: View) {}

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