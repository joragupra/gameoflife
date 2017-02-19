package gol.model

case class Grid (cells: List[Cell])

object Grid {
  def withSize(x: Int, y: Int) = {
    val availableCoordinates = for {
      i <- 0 to x
      j <- 0 to y
    } yield Cell(Coordinates(i, j), Dead)
    Grid(availableCoordinates.toList)
  }
}
