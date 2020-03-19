package de.yochyo.eventcollection

import de.yochyo.eventcollection.events.OnChangeObjectEvent
import de.yochyo.eventcollection.observable.IObservableObject
import de.yochyo.eventmanager.EventHandler
import de.yochyo.eventmanager.Listener

class ObservingSubEventCollection<T : IObservableObject<T, A>, A>(c: MutableCollection<T>, parentCollection: ObservingEventCollection<T, A>, filter: (e: T) -> Boolean) : SubEventCollection<T>(c, parentCollection, filter) {
    private val onChangeListener = Listener.create<OnChangeObjectEvent<T, A>> {
        if (filter(it.new)) onElementChange.trigger(OnChangeObjectEvent(it.new, it.arg))
        else removeFromCollection(it.new)
    }

    val onElementChange = EventHandler<OnChangeObjectEvent<T, A>>()

    init {
        c.forEach { it.onChange.registerListener(onChangeListener) }
        onAddElement.registerListener {
            it.element.onChange.registerListener(onChangeListener)
        }
        onRemoveElement.registerListener {
            it.element.onChange.removeListener(onChangeListener)
        }
    }
}