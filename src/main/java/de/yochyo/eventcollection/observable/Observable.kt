package de.yochyo.eventcollection.observable

import de.yochyo.eventcollection.events.OnChangeObjectEvent
import de.yochyo.eventmanager.EventHandler

class Observable<T>(t: T): IObservableObject<T, T> {
    var value = t
    set(value) {
        onChange.trigger(OnChangeObjectEvent(value, field))
        field = value
    }


    override val onChange = EventHandler<OnChangeObjectEvent<T, T>>()
}