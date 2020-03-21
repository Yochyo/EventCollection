package de.yochyo.eventcollection

import de.yochyo.eventcollection.events.OnAddElementsEvent
import de.yochyo.eventcollection.events.OnClearEvent
import de.yochyo.eventcollection.events.OnRemoveElementsEvent
import de.yochyo.eventmanager.Listener
import java.util.*
import java.util.function.Predicate

/**
 * @see EventCollection
 * An EventCollection that contains all elements of it's parent EventCollection
 * which fulfil the condition that filter() return true.
 * All changes in it's parent are reflected in it.
 * All changes in it are reflected in the parent.
 * For example: If an element is added to the parent EventCollection, it will (if filter() returns true)
 * be added to the SubEventCollection. If an element is added to the parent, it will be added to the SubEventCollection
 *
 * ATTENTION: If you don't need it anymore, call destroy() to remove it's listeners from the parent EventCollection
 *
 * @param parentCollection the parent EventCollection which content should be reflected here
 * @param filter if filter() returns true, an element will be contained in here
 */
open class SubEventCollection<T>(c: MutableCollection<T>, val parentCollection: EventCollection<T>, filter: (e: T) -> Boolean) : EventCollection<T>(c) {
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
    private val onAddElementParent = object : Listener<OnAddElementsEvent<T>>() {
        override fun onEvent(e: OnAddElementsEvent<T>) {
            val add = LinkedList<T>()
            e.elements.forEach { if (filter(it)) add += it }
            addToCollection(add)
        }
    }
    private val onRemoveElementParent = object : Listener<OnRemoveElementsEvent<T>>() {
        override fun onEvent(e: OnRemoveElementsEvent<T>) {
            val remove = LinkedList<T>()
            e.elements.forEach { if (filter(it)) remove += it }
            removeFromCollection(remove)
        }
    }

    protected fun addToCollection(elements: Collection<T>) {
        collection.addAll(elements)
        onAddElements.trigger(OnAddElementsEvent(collection, elements))
    }

    protected fun removeFromCollection(elements: Collection<T>) {
        collection.removeAll(elements)
        onRemoveElements.trigger(OnRemoveElementsEvent(collection, elements))
    }

    init {
        for (e in parentCollection)
            if (filter(e)) c += e

        parentCollection.onClear.registerListener(onClearParent)
        parentCollection.onAddElements.registerListener(onAddElementParent)
        parentCollection.onRemoveElements.registerListener(onRemoveElementParent)
    }

    fun destroy() {
        parentCollection.onClear.removeListener(onClearParent)
        parentCollection.onAddElements.removeListener(onAddElementParent)
        parentCollection.onRemoveElements.removeListener(onRemoveElementParent)
    }
}