package de.yochyo.eventcollection.events

import de.yochyo.eventmanager.Event

class OnUpdateEvent<T>(val collection: Collection<T>) : Event()
class OnClearEvent<T>(val collection: Collection<T>) : Event()
class OnAddElementEvent<T>(val collection: Collection<T>, val element: T) : Event()
class OnRemoveElementEvent<T>(val collection: Collection<T>, val element: T) : Event()


class OnChangeObjectEvent<T, A>(val new: T, val arg: A) : Event()