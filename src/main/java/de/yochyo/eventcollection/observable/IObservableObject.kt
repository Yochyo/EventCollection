package de.yochyo.eventcollection.observable

import de.yochyo.eventcollection.events.OnChangeObjectEvent
import de.yochyo.eventmanager.EventHandler

interface IObservableObject<T, A> {
    val onChange: EventHandler<OnChangeObjectEvent<T, A>>
}