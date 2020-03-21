package de.yochyo.eventcollection

import de.yochyo.eventcollection.events.OnChangeObjectEvent
import de.yochyo.eventcollection.observable.IObservableObject
import de.yochyo.eventmanager.EventHandler
import de.yochyo.eventmanager.Listener

/**
 * @see EventCollection
 * An EventCollection that triggers an OnChangeObjectEvent when an element in it is changed
 * @param T type of elements contained in the collection, must implement IObservableObject
 * @param A type of the arg that is handed over to the OnChangeObjectEvent
 *
 * @property onElementChange triggers an event when an element in the collection is changed (calls an OnChangeObjectEvent)
 */
open class ObservingEventCollection<T : IObservableObject<T, A>, A>(collection: MutableCollection<T>) : EventCollection<T>(collection) {
    private val onChangeListener = Listener.create<OnChangeObjectEvent<T, A>> { onElementChange.trigger(OnChangeObjectEvent(it.new, it.arg)) }
    val onElementChange = object : EventHandler<OnChangeObjectEvent<T, A>>() {
        override fun trigger(e: OnChangeObjectEvent<T, A>) {
            super.trigger(e)
            notifyChange()
        }
    }

    init {
        collection.forEach { it.onChange.registerListener(onChangeListener) }
        onAddElements.registerListener {
            it.elements.forEach { element -> element.onChange.registerListener(onChangeListener) }
        }
        onRemoveElements.registerListener {
            it.elements.forEach { element -> element.onChange.removeListener(onChangeListener) }
        }
    }
}