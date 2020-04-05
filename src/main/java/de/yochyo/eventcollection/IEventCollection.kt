package de.yochyo.eventcollection

import de.yochyo.eventcollection.events.OnAddElementsEvent
import de.yochyo.eventcollection.events.OnClearEvent
import de.yochyo.eventcollection.events.OnRemoveElementsEvent
import de.yochyo.eventcollection.events.OnUpdateEvent
import de.yochyo.eventmanager.Listener

interface IEventCollection<T>: MutableCollection<T> {
    fun registerOnAddElementsListener(l: Listener<OnAddElementsEvent<T>>)
    fun registerOnRemoveElementsListener(l: Listener<OnRemoveElementsEvent<T>>)
    fun registerOnClearListener(l: Listener<OnClearEvent<T>>)
    fun registerOnUpdateListener(l: Listener<OnUpdateEvent<T>>)

    fun registerOnAddElementsListener(priority: Int = Listener.NORMAL, l: (e: OnAddElementsEvent<T>) -> Unit): Listener<OnAddElementsEvent<T>>
    fun registerOnRemoveElementsListener(priority: Int = Listener.NORMAL, l: (e: OnRemoveElementsEvent<T>) -> Unit): Listener<OnRemoveElementsEvent<T>>
    fun registerOnClearListener(priority: Int = Listener.NORMAL, l: (e: OnClearEvent<T>) -> Unit): Listener<OnClearEvent<T>>
    fun registerOnUpdateListener(priority: Int = Listener.NORMAL, l: (e: OnUpdateEvent<T>) -> Unit): Listener<OnUpdateEvent<T>>

    fun removeOnAddElementsListener(l: Listener<OnAddElementsEvent<T>>): Boolean
    fun removeOnRemoveElementsListener(l: Listener<OnRemoveElementsEvent<T>>): Boolean
    fun removeOnClearListener(l: Listener<OnClearEvent<T>>): Boolean
    fun removeOnUpdateListener(l: Listener<OnUpdateEvent<T>>): Boolean
}