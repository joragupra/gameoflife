package gol.services

import scalaz.{NonEmptyList, \/}

trait GridService[Grid, Coordinates, TurnEvent] {

  def createGrid(x: Int, y: Int, initiallyAliveCellsAt: Seq[Coordinates]): Seq[TurnEvent]

  def loadGrid(events: Seq[TurnEvent]): \/[NonEmptyList[String], Grid]

}
