import gol.model._
import gol.services.interpreter.{GridService, ReportService}
import org.scalatest.FlatSpec

class GameTest extends FlatSpec {

  implicit val log: EventLog = new EventLog
  val x = 20
  val y = 20

  {
    GridService.createGrid(x, y,
      Seq(
        Coordinates(0, 1), Coordinates(0, 2), Coordinates(1, 1), Coordinates(2, 1),
        Coordinates(3, 2), Coordinates(3, 1), Coordinates(5, 7),
        Coordinates(5, 8), Coordinates(6, 7), Coordinates(6, 8))
    ).foreach { event => log.add(event) }
  }

  "A grid" should "creates the next generation based on the current one" in {
    for {
      _ <- 1 to 60
    } yield {
      GridService.loadGrid(log.allEvents).foreach(g => {
        val output = ReportService.generateGridReport(g)
        output.run._1.foreach(println)
        Thread.sleep(200)
        GameOfLife.next(g)
        for(_ <- 1 to y) yield println
      })
    }
  }

}
