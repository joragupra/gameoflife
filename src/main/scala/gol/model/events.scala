package gol.model

sealed trait TurnEvent

case class TurnStarted(turn: Int) extends TurnEvent
case class CoordinateEnabled(c: Coordinates) extends TurnEvent
case class CellResurrected(c: Coordinates) extends TurnEvent
case class CellDied(c: Coordinates) extends TurnEvent
