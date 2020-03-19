package de.yochyo.eventcollection

import de.yochyo.eventcollection.events.OnAddElementEvent
import de.yochyo.eventcollection.events.OnClearEvent
import de.yochyo.eventcollection.events.OnRemoveElementEvent
import de.yochyo.eventcollection.events.OnUpdateEvent
import de.yochyo.eventmanager.EventHandler
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * An EventCollection is a collection that triggers events on changes.
 * Uses for it are for example GUI's that work with a changing list
 *
 * @param collection the collection the data is saved into. Depending on your use case, any MutableCollection can be used.
 * This variable is public BUT should not be used if you don't know what you're code is doing or will do, as changes
 * will not trigger events
 * @param T type of elements contained in the collection
 *
 * @property onUpdate triggers an event when the collection is changed
 * @property onClear triggers an event when the collection is cleared
 * @property onAddElement triggers an event when an element is added to the collection
 * @property onRemoveElement triggers an event when an element is removed from the collection
 */
open class EventCollection<T>(@Deprecated("Will Not throw events") val collection: MutableCollection<T>) : MutableCollection<T> {
    val onUpdate = EventHandler<OnUpdateEvent<T>>()
    val onClear = object : EventHandler<OnClearEvent<T>>() {
        override fun trigger(e: OnClearEvent<T>) {
            super.trigger(e)
            notifyChange()
        }
    }
    val onAddElement = object : EventHandler<OnAddElementEvent<T>>() {
        override fun trigger(e: OnAddElementEvent<T>) {
            super.trigger(e)
            notifyChange()
        }
    }
    val onRemoveElement = object : EventHandler<OnRemoveElementEvent<T>>() {
        override fun trigger(e: OnRemoveElementEvent<T>) {
            super.trigger(e)
            notifyChange()
        }
    }

    override val size: Int get() = collection.size
    override fun contains(element: T) = collection.contains(element)
    override fun containsAll(elements: Collection<T>) = collection.containsAll(elements)
    override fun isEmpty() = collection.isEmpty()
    operator fun get(index: Int) = collection.elementAt(index)

    override fun add(element: T): Boolean {
        val res = collection.add(element)
        if (res)
            onAddElement.trigger(OnAddElementEvent(collection, element))
        return res
    }

    override fun addAll(elements: Collection<T>): Boolean {
        var res = false
        for (element in elements) if (add(element)) res = true
        return res
    }

    override fun remove(element: T): Boolean {
        val res = collection.remove(element)
        if (res)
            onRemoveElement.trigger(OnRemoveElementEvent(collection, element))
        return res
    }

    override fun clear() {
        collection.clear()
        onClear.trigger(OnClearEvent(collection))
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var removed = false
        for (e in elements) if (remove(e)) removed = true
        return removed
    }

    override fun removeIf(filter: Predicate<in T>): Boolean {
        var removed = false
        val iter = iterator()
        while (iter.hasNext()) {
            if (filter.test(iter.next())) {
                iter.remove()
                removed = true
            }
        }
        return removed
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val iter = collection.iterator()
        var removed = false
        while (iter.hasNext()) {
            val current = iter.next()
            if (!elements.contains(current)) {
                iter.remove()
                onRemoveElement.trigger(OnRemoveElementEvent(collection, current))
                removed = true
            }
        }
        return removed
    }

    override fun iterator(): MutableIterator<T> = getIterator(collection)

    protected fun getIterator(c: MutableCollection<T>): MutableIterator<T> {
        val i = c.iterator()
        return object : MutableIterator<T> {
            private var current: T? = null
            override fun hasNext() = i.hasNext()
            override fun next(): T {
                val next = i.next()
                current = next
                return next
            }

            override fun remove() {
                i.remove()
                val element = current
                if (element != null)
                    onRemoveElement.trigger(OnRemoveElementEvent(collection, element))
            }
        }
    }

    fun notifyChange() {
        onUpdate.trigger(OnUpdateEvent(collection))
    }

    override fun stream() = collection.stream()
    override fun forEach(action: Consumer<in T>?) = collection.forEach(action)


    @Deprecated("Will not trigger events")
    override fun parallelStream() = collection.parallelStream()

    @Deprecated("Will not trigger events")
    override fun spliterator() = collection.spliterator()
}
