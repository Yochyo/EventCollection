package de.yochyo.eventcollection

import de.yochyo.eventcollection.events.OnChangeObjectEvent
import de.yochyo.eventcollection.observable.IObservableObject
import de.yochyo.eventmanager.EventHandler
import de.yochyo.eventmanager.Listener

class ObservingEventCollection<T : IObservableObject<T, A>, A>(collection: MutableCollection<T>) : EventCollection<T>(collection) {
    private val onChangeListener = Listener.create<OnChangeObjectEvent<T, A>> { onElementChange.trigger(OnChangeObjectEvent(it.new, it.arg)) }
    val onElementChange = EventHandler<OnChangeObjectEvent<T, A>>()

    init {
        collection.forEach { it.onChange.registerListener(onChangeListener) }
        onAddElement.registerListener {
            it.element.onChange.registerListener(onChangeListener)
        }
        onRemoveElement.registerListener {
            it.element.onChange.removeListener(onChangeListener)
        }
    }
}