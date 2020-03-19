package de.yochyo.eventcollection

import de.yochyo.eventcollection.events.OnAddElementEvent
import de.yochyo.eventcollection.events.OnClearEvent
import de.yochyo.eventcollection.events.OnRemoveElementEvent
import de.yochyo.eventmanager.Listener
import java.util.function.Predicate
class SubEventCollection<T>(c: MutableCollection<T>, val parentCollection: EventCollection<T>, filter: (e: T) -> Boolean) : EventCollection<T>(c) {
    override fun add(element: T) = parentCollection.add(element)
    override fun addAll(elements: Collection<T>) = parentCollection.addAll(elements)
    override fun remove(element: T) = parentCollection.remove(element)
    override fun clear() = parentCollection.clear()
    override fun removeAll(elements: Collection<T>) = parentCollection.removeAll(elements)
    override fun removeIf(filter: Predicate<in T>) = parentCollection.removeIf(filter)
    override fun retainAll(elements: Collection<T>) = parentCollection.retainAll(elements)

    private val onClearParent = object : Listener<OnClearEvent<T>>() {
        override fun onEvent(e: OnClearEvent<T>) {
            c.clear()
            onClear.trigger(OnClearEvent(c))
        }
    }
    private val onAddElementParent = object : Listener<OnAddElementEvent<T>>() {
        override fun onEvent(e: OnAddElementEvent<T>) {
            if (filter(e.element)) {
                c.add(e.element)
                onAddElement.trigger(OnAddElementEvent(c, e.element))
            }
        }
    }
    private val onRemoveElementParent = object : Listener<OnRemoveElementEvent<T>>() {
        override fun onEvent(e: OnRemoveElementEvent<T>) {
            if (filter(e.element)) {
                c.remove(e.element)
                onRemoveElement.trigger(OnRemoveElementEvent(c, e.element))
            }
        }
    }

    init {
        for (e in parentCollection)
            if (filter(e)) c += e

        parentCollection.onClear.registerListener(onClearParent)
        parentCollection.onAddElement.registerListener(onAddElementParent)
        parentCollection.onRemoveElement.registerListener(onRemoveElementParent)
    }

    fun destroy() {
        parentCollection.onClear.removeListener(onClearParent)
        parentCollection.onAddElement.removeListener(onAddElementParent)
        parentCollection.onRemoveElement.removeListener(onRemoveElementParent)
    }
}