import gol.model._
import gol.services.interpreter.GenerationService
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class GameOfLifeSpec extends FlatSpec {

  private val originalGrid = Grid.withSize(0, 4)

  "A grid" should "creates the next generation based on the current one" in {
    implicit var testGrid = Grid(List(
      Cell(Coordinates(0, 0), Dead),
      Cell(Coordinates(0, 1), Dead),
      Cell(Coordinates(0, 2), Dead),
      Cell(Coordinates(0, 3), Dead),

      Cell(Coordinates(1, 0), Dead),
      Cell(Coordinates(1, 1), Dead),
      Cell(Coordinates(1, 2), Alive),
      Cell(Coordinates(1, 3), Dead),

      Cell(Coordinates(2, 0), Dead),
      Cell(Coordinates(2, 1), Dead),
      Cell(Coordinates(2, 2), Alive),
      Cell(Coordinates(2, 3), Alive),

      Cell(Coordinates(3, 0), Dead),
      Cell(Coordinates(3, 1), Dead),
      Cell(Coordinates(3, 2), Alive),
      Cell(Coordinates(3, 3), Dead)

    ))

    val nextGenerationGrid = GameOfLife.next(testGrid)

    println(nextGenerationGrid)
  }

}
