
object domain {

  sealed trait State
  case object Alive extends State
  case object Dead extends State
  case class Coordinates(x: Int, y: Int)
  case class Cell(coord: Coordinates, s: State)
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

}
