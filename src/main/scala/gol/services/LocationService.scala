package gol.services

import scalaz.Reader

trait LocationService[Grid, Cell, Coordinates, State] {
  def areNeighbours(c1: Coordinates, c2: Coordinates): Boolean

  def neighbours(coord: Coordinates): Reader[Grid, List[Cell]]

  def countNeighbours(coord: Coordinates, s: State): Reader[Grid, Int]

  def find(coord: Coordinates): Reader[Grid, Option[Cell]]
}
