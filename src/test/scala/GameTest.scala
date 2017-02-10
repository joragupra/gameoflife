import domain._
import org.scalatest.FlatSpec

class GameTest extends FlatSpec {

  "A grid" should "creates the next generation based on the current one" in {
    var g: Grid = Grid.apply(
      List(
        Cell(Coordinates(0,0), Dead),         Cell(Coordinates(0,1), Alive),         Cell(Coordinates(0,2), Dead),
        Cell(Coordinates(1,0), Dead),         Cell(Coordinates(1,1), Alive),         Cell(Coordinates(1,2), Dead),
        Cell(Coordinates(2,0), Dead),         Cell(Coordinates(2,1), Alive),         Cell(Coordinates(2,2), Alive)
      ))

    for {
      i <- 1 to 10
    } yield {
      PrintService.printGrid(g)
      println("********* Next Generation ************")
      g = GameOfLife.next(g).get
    }
  }

}