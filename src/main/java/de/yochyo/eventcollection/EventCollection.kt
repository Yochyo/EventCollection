package de.yochyo.eventcollection

import de.yochyo.eventmanager.Event
import de.yochyo.eventmanager.EventHandler
import java.util.function.Predicate


open class EventCollection<T>(@Deprecated("Will Not throw events") val collection: MutableCollection<T>) : MutableCollection<T> {
    val onUpdate = object : EventHandler<OnUpdateEvent>() {}
    val onClear = object : EventHandler<OnClearEvent>() {
        override fun trigger(e: EventCollection<T>.OnClearEvent) {
            super.trigger(e)
            notifyChange()
        }
    }
    val onAddElement = object : EventHandler<OnAddElementEvent>() {
        override fun trigger(e: EventCollection<T>.OnAddElementEvent) {
            super.trigger(e)
            notifyChange()
        }
    }
    val onRemoveElement = object : EventHandler<OnRemoveElementEvent>() {
        override fun trigger(e: EventCollection<T>.OnRemoveElementEvent) {
            super.trigger(e)
            notifyChange()
        }
    }

    init {
        notifyChange()
    }

    fun notifyChange() {
        onUpdate.trigger(OnUpdateEvent(collection))
    }

    override val size: Int get() = collection.size
    override fun contains(element: T) = collection.contains(element)
    override fun containsAll(elements: Collection<T>) = collection.containsAll(elements)
    override fun isEmpty() = collection.isEmpty()
    operator fun get(index: Int) = collection.elementAt(index)

    override fun add(e: T): Boolean {
        val res = collection.add(e)
        if (res)
            onAddElement.trigger(OnAddElementEvent(collection, e))
        return res
    }

    override fun addAll(e: Collection<T>): Boolean {
        var res = false
        for(element in e) if(add(element)) res = true
        return res
    }

    override fun remove(e: T): Boolean {
        val res = collection.remove(e)
        if (res)
            onRemoveElement.trigger(OnRemoveElementEvent(collection, e))
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
        val iter = collection.iterator()
        while (iter.hasNext()) {
            val current = iter.next()
            if (filter.test(current)) {
                iter.remove()
                removed = true
                onRemoveElement.trigger(OnRemoveElementEvent(collection, current))
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


    override fun iterator() = collection.iterator()
    @Deprecated("Will not trigger events")
    override fun parallelStream() = collection.parallelStream()

    @Deprecated("Will not trigger events")
    override fun spliterator() = collection.spliterator()

    inner class OnUpdateEvent(val collection: Collection<T>) : Event()
    inner class OnClearEvent(val collection: Collection<T>) : Event()
    inner class OnAddElementEvent(val collection: Collection<T>, val element: T) : Event()
    inner class OnRemoveElementEvent(val collection: Collection<T>, val element: T) : Event()
}
