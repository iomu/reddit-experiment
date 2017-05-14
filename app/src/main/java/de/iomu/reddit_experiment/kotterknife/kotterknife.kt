package de.iomu.reddit_experiment.kotterknife

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.app.Fragment
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import com.bluelinelabs.conductor.Controller
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import android.support.v4.app.DialogFragment as SupportDialogFragment
import android.support.v4.app.Fragment as SupportFragment

// Adapted from https://github.com/JakeWharton/kotterknife

fun <V : View> View.bindView(id: Int)
        : ReadOnlyProperty<View, V> = required(id, viewFinder)
fun <V : View> Activity.bindView(id: Int)
        : ReadOnlyProperty<Activity, V> = required(id, viewFinder)
fun <V : View> Dialog.bindView(id: Int)
        : ReadOnlyProperty<Dialog, V> = required(id, viewFinder)
fun <V : View> DialogFragment.bindView(id: Int)
        : ReadOnlyProperty<DialogFragment, V> = required(id, viewFinder)
fun <V : View> SupportDialogFragment.bindView(id: Int)
        : ReadOnlyProperty<SupportDialogFragment, V> = required(id, viewFinder)
fun <V : View> Fragment.bindView(id: Int)
        : ReadOnlyProperty<Fragment, V> = required(id, viewFinder)
fun <V : View> SupportFragment.bindView(id: Int)
        : ReadOnlyProperty<SupportFragment, V> = required(id, viewFinder)
fun <V : View> ViewHolder.bindView(id: Int)
        : ReadOnlyProperty<ViewHolder, V> = required(id, viewFinder)
fun <V : View> Controller.bindView(id: Int, resetter: Resetter)
        : ReadOnlyProperty<Controller, V> = required(id, viewFinder, resetter)

fun <V : View> View.bindOptionalView(id: Int)
        : ReadOnlyProperty<View, V?> = optional(id, viewFinder)
fun <V : View> Activity.bindOptionalView(id: Int)
        : ReadOnlyProperty<Activity, V?> = optional(id, viewFinder)
fun <V : View> Dialog.bindOptionalView(id: Int)
        : ReadOnlyProperty<Dialog, V?> = optional(id, viewFinder)
fun <V : View> DialogFragment.bindOptionalView(id: Int)
        : ReadOnlyProperty<DialogFragment, V?> = optional(id, viewFinder)
fun <V : View> SupportDialogFragment.bindOptionalView(id: Int)
        : ReadOnlyProperty<SupportDialogFragment, V?> = optional(id, viewFinder)
fun <V : View> Fragment.bindOptionalView(id: Int)
        : ReadOnlyProperty<Fragment, V?> = optional(id, viewFinder)
fun <V : View> SupportFragment.bindOptionalView(id: Int)
        : ReadOnlyProperty<SupportFragment, V?> = optional(id, viewFinder)
fun <V : View> ViewHolder.bindOptionalView(id: Int)
        : ReadOnlyProperty<ViewHolder, V?> = optional(id, viewFinder)
fun <V : View> Controller.bindOptionalView(id: Int, resetter: Resetter)
        : ReadOnlyProperty<Controller, V?> = optional(id, viewFinder, resetter)

fun <V : View> View.bindViews(vararg ids: Int)
        : ReadOnlyProperty<View, List<V>> = required(ids, viewFinder)
fun <V : View> Activity.bindViews(vararg ids: Int)
        : ReadOnlyProperty<Activity, List<V>> = required(ids, viewFinder)
fun <V : View> Dialog.bindViews(vararg ids: Int)
        : ReadOnlyProperty<Dialog, List<V>> = required(ids, viewFinder)
fun <V : View> DialogFragment.bindViews(vararg ids: Int)
        : ReadOnlyProperty<DialogFragment, List<V>> = required(ids, viewFinder)
fun <V : View> SupportDialogFragment.bindViews(vararg ids: Int)
        : ReadOnlyProperty<SupportDialogFragment, List<V>> = required(ids, viewFinder)
fun <V : View> Fragment.bindViews(vararg ids: Int)
        : ReadOnlyProperty<Fragment, List<V>> = required(ids, viewFinder)
fun <V : View> SupportFragment.bindViews(vararg ids: Int)
        : ReadOnlyProperty<SupportFragment, List<V>> = required(ids, viewFinder)
fun <V : View> ViewHolder.bindViews(vararg ids: Int)
        : ReadOnlyProperty<ViewHolder, List<V>> = required(ids, viewFinder)
fun <V : View> Controller.bindViews(vararg ids: Int, resetter: Resetter)
        : ReadOnlyProperty<Controller, List<V>> = required(ids, viewFinder, resetter)

fun <V : View> View.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<View, List<V>> = optional(ids, viewFinder)
fun <V : View> Activity.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<Activity, List<V>> = optional(ids, viewFinder)
fun <V : View> Dialog.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<Dialog, List<V>> = optional(ids, viewFinder)
fun <V : View> DialogFragment.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<DialogFragment, List<V>> = optional(ids, viewFinder)
fun <V : View> SupportDialogFragment.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<SupportDialogFragment, List<V>> = optional(ids, viewFinder)
fun <V : View> Fragment.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<Fragment, List<V>> = optional(ids, viewFinder)
fun <V : View> SupportFragment.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<SupportFragment, List<V>> = optional(ids, viewFinder)
fun <V : View> ViewHolder.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<ViewHolder, List<V>> = optional(ids, viewFinder)
fun <V : View> Controller.bindOptionalViews(vararg ids: Int, resetter: Resetter)
        : ReadOnlyProperty<Controller, List<V>> = optional(ids, viewFinder, resetter)

private val View.viewFinder: View.(Int) -> View?
    get() = { findViewById(it) }
private val Activity.viewFinder: Activity.(Int) -> View?
    get() = { findViewById(it) }
private val Dialog.viewFinder: Dialog.(Int) -> View?
    get() = { findViewById(it) }
private val DialogFragment.viewFinder: DialogFragment.(Int) -> View?
    get() = { dialog.findViewById(it) }
private val SupportDialogFragment.viewFinder: SupportDialogFragment.(Int) -> View?
    get() = { dialog.findViewById(it) }
private val Fragment.viewFinder: Fragment.(Int) -> View?
    get() = { view.findViewById(it) }
private val SupportFragment.viewFinder: SupportFragment.(Int) -> View?
    get() = { view!!.findViewById(it) }
private val ViewHolder.viewFinder: ViewHolder.(Int) -> View?
    get() = { itemView.findViewById(it) }
private val Controller.viewFinder: Controller.(Int) -> View?
    get() = { view?.findViewById(it) }

private fun viewNotFound(id:Int, desc: KProperty<*>): Nothing =
        throw IllegalStateException("View ID $id for '${desc.name}' not found.")

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(id: Int, finder: T.(Int) -> View?, resetter: Resetter? = null)
        = Lazy(resetter) { t: T, desc -> t.finder(id) as V? ?: viewNotFound(id, desc) }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(id: Int, finder: T.(Int) -> View?, resetter: Resetter? = null)
        = Lazy(resetter) { t: T, desc ->  t.finder(id) as V? }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(ids: IntArray, finder: T.(Int) -> View?, resetter: Resetter? = null)
        = Lazy(resetter) { t: T, desc -> ids.map { t.finder(it) as V? ?: viewNotFound(it, desc) } }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(ids: IntArray, finder: T.(Int) -> View?, resetter: Resetter? = null)
        = Lazy(resetter) { t: T, desc -> ids.map { t.finder(it) as V? }.filterNotNull() }

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
private class Lazy<T, V>(resetter: Resetter? = null, private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY
    private var value: Any? = EMPTY

    init {
        resetter?.addListener { value = EMPTY }
    }

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}

class Resetter {
    private val listeners: MutableList<() -> Unit> = mutableListOf()

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun reset() = listeners.forEach { it() }
}