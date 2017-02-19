package gol.services.interpreter

import gol.model.{Cell, Coordinates, Grid, State}
import gol.services.LocationService

import scalaz.Reader

abstract class LocationServiceInterpreter extends LocationService[Grid, Cell, Coordinates, State] {
  import Math.abs

  override def areNeighbours(c1: Coordinates, c2: Coordinates): Boolean = (c1, c2) match {
    case (Coordinates(x1, y1), Coordinates(x2, y2)) if x1 == x2 => abs(y1 - y2) == 1
    case (Coordinates(x1, y1), Coordinates(x2, y2)) if y1 == y2 => abs(x1 - x2) == 1
    case (Coordinates(x1, y1), Coordinates(x2, y2)) => abs(x1 - x2) == 1 && abs(y1 - y2) == 1
    case (_, _) => false
  }

  override def neighbours(coord: Coordinates): Reader[Grid, List[Cell]] =
    Reader {
      (g: Grid) => g.cells.filter(cell => areNeighbours(cell.coord, coord))
    }

  override def countNeighbours(coord: Coordinates, state: State): Reader[Grid, Int] = neighbours(coord).map(cells => cells.count(_.s == state))

  def find(coord: Coordinates): Reader[Grid, Option[Cell]] =
    Reader {
      (g: Grid) => g.cells.find(_.coord == coord)
    }

}

object LocationService extends LocationServiceInterpreter
