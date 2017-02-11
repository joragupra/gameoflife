import gol.model._
import gol.services.interpreter.GenerationService
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class GameOfLifeSpec extends FlatSpec {

  private val originalGrid = Grid.withSize(0, 4)

  "A grid" should "creates the next generation based on the current one" in {
    implicit var testGrid = GenerationService.resurrect(Coordinates(2, 2))(originalGrid)
    testGrid = GenerationService.resurrect(Coordinates(2, 3))
    testGrid = GenerationService.resurrect(Coordinates(1, 2))
    testGrid = GenerationService.resurrect(Coordinates(3, 2))

    val nextGenerationGrid = GameOfLife.next(testGrid)

    println(nextGenerationGrid)
  }

}
