package gol.services

import scalaz.Reader

trait GenerationService[Grid, Cell, Coordinates, State, TurnEvent] {

  def nextGeneration(cell:Cell): Reader[Grid, Option[TurnEvent]]

  def underpopulation(cell: Cell): Reader[Grid, Option[TurnEvent]]

  def survival(cell: Cell): Reader[Grid, Option[TurnEvent]]

  def overpopulation(cell: Cell): Reader[Grid, Option[TurnEvent]]

  def reproduction(cell: Cell): Reader[Grid, Option[TurnEvent]]

}
