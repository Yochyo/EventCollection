package de.yochyo.eventcollection

import de.yochyo.eventcollection.events.*
import de.yochyo.eventmanager.Listener

interface IEventCollection<T> : MutableCollection<T> {
    fun replaceCollection(c: MutableCollection<T>)

    fun registerOnAddElementsListener(l: Listener<OnAddElementsEvent<T>>)
    fun registerOnRemoveElementsListener(l: Listener<OnRemoveElementsEvent<T>>)
    fun registerOnClearListener(l: Listener<OnClearEvent<T>>)
    fun registerOnUpdateListener(l: Listener<OnUpdateEvent<T>>)
    fun registerOnReplaceCollectionListener(l: Listener<OnReplaceCollectionEvent<T>>)

    fun removeOnAddElementsListener(l: Listener<OnAddElementsEvent<T>>)
    fun removeOnRemoveElementsListener(l: Listener<OnRemoveElementsEvent<T>>)
    fun removeOnClearListener(l: Listener<OnClearEvent<T>>)
    fun removeOnUpdateListener(l: Listener<OnUpdateEvent<T>>)
    fun removeOnReplaceCollectionListener(l: Listener<OnReplaceCollectionEvent<T>>)

    fun triggerOnAddElementsEvent(e: OnAddElementsEvent<T>)
    fun triggerOnRemoveElementsEvent(e: OnRemoveElementsEvent<T>)
    fun triggerOnClearEvent(e: OnClearEvent<T>)
    fun triggerOnUpdateEvent(e: OnUpdateEvent<T>)
    fun triggerOnReplaceCollectionEvent(e: OnReplaceCollectionEvent<T>)
}