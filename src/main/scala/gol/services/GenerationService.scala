package gol.services

import scalaz.Reader

trait GenerationService[Grid, Cell, Coordinates, State] {
  def nextGeneration(cell:Cell): Reader[Grid, Option[Cell]]

  def underpopulation(cell: Cell): Reader[Grid, Option[Cell]]

  def survival(cell: Cell): Reader[Grid, Option[Cell]]

  def overpopulation(cell: Cell): Reader[Grid, Option[Cell]]

  def reproduction(cell: Cell): Reader[Grid, Option[Cell]]
}
