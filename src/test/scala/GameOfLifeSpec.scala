import domain._
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class GameOfLifeSpec extends FlatSpec {

  private val originalGrid = Grid.withSize(0, 4)

  "A living cell" should "die if it has less than two living neighbours" in {
    implicit val testGrid = GameOfLife.resurrect(Coordinates(2, 2))(originalGrid)

    val nextGenerationCell = GameOfLife.next(Cell(Coordinates(2, 2), Alive))

    nextGenerationCell.get.s should be(Dead)
  }

  it should "live if it has two or three neighbours" in {
    implicit var testGrid = GameOfLife.resurrect(Coordinates(2, 2))(originalGrid)
    testGrid = GameOfLife.resurrect(Coordinates(2, 3))
    testGrid = GameOfLife.resurrect(Coordinates(1, 2))

    val nextGenerationCell = GameOfLife.next(Cell(Coordinates(2, 2), Alive))

    nextGenerationCell.get.s should be(Alive)
  }

  it should "should die if it has more than three neighbours" in {
    implicit var testGrid = GameOfLife.resurrect(Coordinates(2, 2))(originalGrid)
    testGrid = GameOfLife.resurrect(Coordinates(2, 3))
    testGrid = GameOfLife.resurrect(Coordinates(1, 2))
    testGrid = GameOfLife.resurrect(Coordinates(3, 2))
    testGrid = GameOfLife.resurrect(Coordinates(3, 3))


    val nextGenerationCell = GameOfLife.next(Cell(Coordinates(2, 2), Alive))

    nextGenerationCell.get.s should be(Dead)
  }

  "A dead cell" should "become a live cell if it has exactly three neighbours" in {
    implicit var testGrid = GameOfLife.kill(Coordinates(2, 2))(originalGrid)
    testGrid = GameOfLife.resurrect(Coordinates(2, 3))
    testGrid = GameOfLife.resurrect(Coordinates(1, 2))
    testGrid = GameOfLife.resurrect(Coordinates(3, 2))


    val nextGenerationCell = GameOfLife.next(Cell(Coordinates(2, 2), Dead))

    nextGenerationCell.get.s should be(Alive)
  }

  "A grid" should "creates the next generation based on the current one" in {
    implicit var testGrid = GameOfLife.resurrect(Coordinates(2, 2))(originalGrid)
    testGrid = GameOfLife.resurrect(Coordinates(2, 3))
    testGrid = GameOfLife.resurrect(Coordinates(1, 2))
    testGrid = GameOfLife.resurrect(Coordinates(3, 2))

    val nextGenerationGrid = GameOfLife.next

    println(nextGenerationGrid)
  }

}
