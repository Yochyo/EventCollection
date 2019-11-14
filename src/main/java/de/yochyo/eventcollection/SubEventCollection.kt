package de.yochyo.eventcollection

import de.yochyo.eventmanager.Listener
import java.util.function.Predicate
class SubEventCollection<T>(c: MutableCollection<T>, val parentCollection: EventCollection<T>, filter: (e: T) -> Boolean) : EventCollection<T>(c) {
    override fun add(e: T) = parentCollection.add(e)
    override fun addAll(e: Collection<T>) = parentCollection.addAll(e)
    override fun remove(e: T) = parentCollection.remove(e)
    override fun clear() = parentCollection.clear()
    override fun removeAll(elements: Collection<T>) = parentCollection.removeAll(elements)
    override fun removeIf(filter: Predicate<in T>) = parentCollection.removeIf(filter)
    override fun retainAll(elements: Collection<T>) = parentCollection.retainAll(elements)

    private val onClearParent = object : Listener<OnClearEvent>() {
        override fun onEvent(e: EventCollection<T>.OnClearEvent) {
            c.clear()
            onClear.trigger(OnClearEvent(c))
        }
    }
    private val onAddElementParent = object : Listener<OnAddElementEvent>() {
        override fun onEvent(e: EventCollection<T>.OnAddElementEvent) {
            if (filter(e.element)) {
                c.add(e.element)
                onAddElement.trigger(OnAddElementEvent(c, e.element))
            }
        }
    }
    private val onRemoveElementParent = object : Listener<OnRemoveElementEvent>() {
        override fun onEvent(e: EventCollection<T>.OnRemoveElementEvent) {
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