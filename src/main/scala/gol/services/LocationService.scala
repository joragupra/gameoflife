package gol.services

import gol.model.{Cell, Grid}

import scalaz.Reader

trait LocationService[Coordinates, State] {
  def areNeighbours(c1: Coordinates, c2: Coordinates): Boolean

  def neighbours(coord: Coordinates): Reader[Grid, List[Cell]]

  def countNeighbours(coord: Coordinates, s: State): Reader[Grid, Int]
}
