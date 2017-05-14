package de.iomu.reddit_experiment.base

import com.bluelinelabs.conductor.Controller
import dagger.android.DispatchingAndroidInjector

interface HasDispatchingControllerInjector {
    fun controllerInjector(): DispatchingAndroidInjector<Controller>
}