import gol.model._
import gol.services.interpreter.{GenerationService, GridService}

object GameOfLife {

  implicit val log: EventLog  = new EventLog

  def next(g: Grid)(implicit log: EventLog): Option[Grid] = {
    val events = g.cells.map(GenerationService.nextGeneration(_).run(g)).filter(maybeEvent => maybeEvent.isDefined)

    events.foreach(e => e.foreach(log.add))

    load(log)
  }

  private def load(log: EventLog): Option[Grid] = {
    val grid = GridService.loadGrid(log.allEvents)
    if (grid.isRight) {
      Some(GridService.loadGrid(log.allEvents).toEither.right.get)
    } else {
      println("ERROR!")
      println(grid.swap.foreach(errors => errors.foreach(println)))
      None
    }
  }

}
