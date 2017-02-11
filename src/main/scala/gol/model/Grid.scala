package gol.model

case class Grid (cells: List[Cell])

object Grid {
  def withSize(min: Int, max: Int) = {
    val availableCoordinates = for {
      i <- min to max
      j <- min to max
    } yield Cell(Coordinates(i, j), Dead)
    Grid(availableCoordinates.toList)
  }
}
