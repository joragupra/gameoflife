package gol.model

sealed trait State
case object Alive extends State
case object Dead extends State

case class Coordinates(x: Int, y: Int)

case class Cell(coord: Coordinates, s: State)