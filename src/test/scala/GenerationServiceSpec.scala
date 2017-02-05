import domain._
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class GenerationServiceSpec extends FlatSpec {

  implicit val grid = Grid.withSize(0, 10)

  "The cell in a coordinate" should "become dead if it is killed" in {
    val coord = Coordinates(2, 3)

    val changedGrid = GameOfLife.kill(coord)

    findCell(coord, changedGrid).get.s should be(Dead)
  }

  "The cell in a coordinate" should "become alive if it is resurrected" in {
    val coord = Coordinates(2, 3)

    val changedGrid = GameOfLife.resurrect(coord)

    findCell(coord, changedGrid).get.s should be(Alive)
  }

  private def findCell(coordinates: Coordinates, grid: Grid): Option[Cell] = grid.cells.find(_.coord == coordinates)

}
