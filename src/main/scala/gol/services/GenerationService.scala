package gol.services

import scalaz.Reader

trait GenerationService[Grid, Cell, Coordinates, State] {
  def kill(coord: Coordinates)(implicit g: Grid): Grid

  def resurrect(coord: Coordinates)(implicit g: Grid): Grid

  def nextGeneration(cell:Cell): Reader[Grid, Option[Cell]]

  def underpopulation(cell: Cell): Reader[Grid, Option[Cell]]

  def survival(cell: Cell): Reader[Grid, Option[Cell]]

  def overpopulation(cell: Cell): Reader[Grid, Option[Cell]]

  def reproduction(cell: Cell): Reader[Grid, Option[Cell]]
}
