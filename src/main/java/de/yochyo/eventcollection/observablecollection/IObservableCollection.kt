package de.yochyo.eventcollection.observablecollection

import de.yochyo.eventcollection.IEventCollection
import de.yochyo.eventcollection.events.OnChangeObjectEvent
import de.yochyo.eventmanager.Listener

interface IObservableCollection<E, A>: IEventCollection<E>{
    fun registerOnElementChangeListener(l: Listener<OnChangeObjectEvent<E, A>>)
    fun registerOnElementChangeListener(priority: Int = Listener.NORMAL, l: (e: OnChangeObjectEvent<E, A>) -> Unit): Listener<OnChangeObjectEvent<E, A>>
    fun removeOnElementChangeListener(l: Listener<OnChangeObjectEvent<E, A>>): Boolean
}