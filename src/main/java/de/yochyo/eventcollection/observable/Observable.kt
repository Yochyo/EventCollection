package de.yochyo.eventcollection.observable

import de.yochyo.eventcollection.events.OnChangeObjectEvent
import de.yochyo.eventmanager.EventHandler

class Observable<T>(t: T) : IObservableObject<Observable<T>, T> {
    var value = t
        set(value) {
            onChange.trigger(OnChangeObjectEvent(this, field))
            field = value
        }


    override val onChange = EventHandler<OnChangeObjectEvent<Observable<T>, T>>()
    override fun toString() = value.toString()
    override fun hashCode() = value.hashCode()
    override fun equals(other: Any?) = value?.equals(other) ?: false
}