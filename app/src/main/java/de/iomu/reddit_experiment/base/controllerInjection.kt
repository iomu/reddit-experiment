package de.iomu.reddit_experiment.base

import dagger.Module
import com.bluelinelabs.conductor.Controller
import dagger.MapKey
import dagger.android.AndroidInjector
import dagger.multibindings.Multibinds
import javax.inject.Scope
import kotlin.reflect.KClass


@Module
abstract class ConductorInjectionModule {

    @Multibinds
    abstract fun controllerInjectorFactories(): Map<Class<out Controller>, AndroidInjector.Factory<out Controller>>
}

@MapKey @Target(AnnotationTarget.FUNCTION)
annotation class ControllerKey(val value: KClass<out Controller>)

@Scope
annotation class ControllerScope