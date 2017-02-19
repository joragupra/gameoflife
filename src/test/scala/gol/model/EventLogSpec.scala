package gol.model

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class EventLogSpec extends FlatSpec {

  "An turn event" should "be available after being added to the EventLog" in {
    val log = new EventLog

    val initialEvents = log.allEvents

    log.add(TurnStarted(1))

    val finalEvents = log.allEvents

    finalEvents.diff(initialEvents).size should be(1)
    finalEvents.diff(initialEvents).head should be(TurnStarted(1))
  }

  "All events" should "be retrieved in the same order they were added" in {
    val log = new EventLog

    log.add(TurnStarted(1))
    log.add(CellDied(Coordinates(0, 0)))
    log.add(CellResurrected(Coordinates(0, 1)))

    log.allEvents.head should be(TurnStarted(1))
    log.allEvents(1) should be(CellDied(Coordinates(0, 0)))
    log.allEvents(2) should be(CellResurrected(Coordinates(0, 1)))
  }

}
