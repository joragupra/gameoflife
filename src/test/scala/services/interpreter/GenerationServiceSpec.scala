package services.interpreter

import gol.model._
import gol.services.interpreter.GenerationService
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class GenerationServiceSpec extends FlatSpec {

  "The cell in a coordinate" should "become dead if it is killed" in {
    implicit val grid = Grid.withSize(0, 10)

    val coord = Coordinates(2, 3)

    val changedGrid = GenerationService.kill(coord)

    findCell(coord, changedGrid).get.s should be(Dead)
  }

  "The cell in a coordinate" should "become alive if it is resurrected" in {
    implicit val grid = Grid.withSize(0, 10)

    val coord = Coordinates(2, 3)

    val changedGrid = GenerationService.resurrect(coord)

    findCell(coord, changedGrid).get.s should be(Alive)
  }

  private def findCell(coordinates: Coordinates, grid: Grid): Option[Cell] = grid.cells.find(_.coord == coordinates)

  implicit private val originalGrid = Grid.withSize(0, 4)

  "A living cell" should "die if it has less than two living neighbours" in {
    val testCell = Cell(Coordinates(2, 2), Alive)
    val testGrid = GenerationService.resurrect(testCell.coord)(originalGrid)

    val nextGenerationCell = GenerationService.underpopulation(testCell)

    nextGenerationCell.run(testGrid).get.s should be(Dead)
  }

  it should "live if it has two or three neighbours" in {
    val testCell = Cell(Coordinates(2, 2), Alive)
    var testGrid = GenerationService.resurrect(testCell.coord)(originalGrid)
    testGrid = GenerationService.resurrect(Coordinates(2, 3))(testGrid)
    testGrid = GenerationService.resurrect(Coordinates(1, 2))(testGrid)

    val nextGenerationCell = GenerationService.survival(testCell)

    nextGenerationCell.run(testGrid).get.s should be(Alive)
  }

  it should "should die if it has more than three neighbours" in {
    val testCell = Cell(Coordinates(2, 2), Alive)
    var testGrid = GenerationService.resurrect(Coordinates(2, 2))(originalGrid)
    testGrid = GenerationService.resurrect(Coordinates(2, 3))(testGrid)
    testGrid = GenerationService.resurrect(Coordinates(1, 2))(testGrid)
    testGrid = GenerationService.resurrect(Coordinates(3, 2))(testGrid)
    testGrid = GenerationService.resurrect(Coordinates(3, 3))(testGrid)

    val nextGenerationCell = GenerationService.overpopulation(testCell)

    nextGenerationCell.run(testGrid).get.s should be(Dead)
  }

  "A dead cell" should "become a live cell if it has exactly three neighbours" in {
    val testCell = Cell(Coordinates(2, 2), Dead)
    var testGrid = GenerationService.kill(Coordinates(2, 2))(originalGrid)
    testGrid = GenerationService.resurrect(Coordinates(2, 3))(testGrid)
    testGrid = GenerationService.resurrect(Coordinates(1, 2))(testGrid)
    testGrid = GenerationService.resurrect(Coordinates(3, 2))(testGrid)

    val nextGenerationCell = GenerationService.reproduction(testCell)

    nextGenerationCell.run(testGrid).get.s should be(Alive)
  }

}
