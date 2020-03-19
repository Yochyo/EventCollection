package de.yochyo.eventcollection.observable

import de.yochyo.eventcollection.events.OnChangeObjectEvent
import de.yochyo.eventmanager.EventHandler

class Observable<T>(t: T) : IObservableObject<Observable<T>, T> {
    var value = t
        set(value) {
            val oldValue = field
            field = value
            onChange.trigger(OnChangeObjectEvent(this, oldValue))
        }


    override val onChange = EventHandler<OnChangeObjectEvent<Observable<T>, T>>()
    override fun toString() = value.toString()
}