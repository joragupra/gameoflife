import gol.model._
import gol.services.interpreter.GridService
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class GameOfLifeSpec extends FlatSpec {

  implicit val log: EventLog = new EventLog

  {
    GridService.createGrid(4, 4, Seq(Coordinates(0, 1), Coordinates(0, 2), Coordinates(1, 1), Coordinates(2, 1),
      Coordinates(3, 3), Coordinates(3, 2), Coordinates(3, 1))).foreach {
      event => log.add(event)
    }
  }

  "A grid" should "creates the next generation based on the current one" in {
    GridService.loadGrid(log.allEvents).foreach(
      initialGeneration => {
        println(initialGeneration)
        println(".....")
        println(GameOfLife.next(initialGeneration))
      }
    )
  }

}
