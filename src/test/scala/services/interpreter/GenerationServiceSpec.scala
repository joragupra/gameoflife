package services.interpreter

import gol.model._
import gol.services.interpreter.GenerationService
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class GenerationServiceSpec extends FlatSpec {

  "A living cell" should "die if it has less than two living neighbours" in {
    val testCell = Cell(Coordinates(2, 2), Alive)
    val testGrid = Grid(List(testCell))

    val nextGenerationCell = GenerationService.underpopulation(testCell)

    nextGenerationCell.run(testGrid).get should be(CellDied(testCell.coord))
  }

  it should "live if it has two or three neighbours" in {
    val testCell = Cell(Coordinates(2, 2), Alive)
    val testGrid = Grid(List(testCell, Cell(Coordinates(2, 3), Alive), Cell(Coordinates(1, 2), Alive)))

    val nextGenerationCell = GenerationService.survival(testCell)

    nextGenerationCell.run(testGrid) should be(None)
  }

  it should "should die if it has more than three neighbours" in {
    val testCell = Cell(Coordinates(2, 2), Alive)
    val testGrid = Grid(List(testCell, Cell(Coordinates(2, 3), Alive), Cell(Coordinates(1, 2), Alive), Cell(Coordinates(3, 2), Alive), Cell(Coordinates(3, 3), Alive)))

    val nextGenerationCell = GenerationService.overpopulation(testCell)

    nextGenerationCell.run(testGrid).get should be(CellDied(testCell.coord))
  }

  "A dead cell" should "become a live cell if it has exactly three neighbours" in {
    val testCell = Cell(Coordinates(2, 2), Dead)
    val testGrid = Grid(List(testCell, Cell(Coordinates(3, 3), Alive), Cell(Coordinates(1, 3), Alive), Cell(Coordinates(2, 3), Alive)))

    val nextGenerationCell = GenerationService.reproduction(testCell)

    nextGenerationCell.run(testGrid).get should be(CellResurrected(testCell.coord))
  }

}
