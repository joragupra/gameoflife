package gol.model

import scala.collection.mutable

class EventLog {

  def allEvents: Seq[TurnEvent] = events.toList

  def reset: Unit = events = mutable.MutableList()

  def add(event: TurnEvent): Unit = events += event

  private var events: mutable.MutableList[TurnEvent] = mutable.MutableList()

}
