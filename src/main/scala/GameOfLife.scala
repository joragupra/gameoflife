import gol.model._
import gol.services.interpreter.{GenerationService, GridService}

object GameOfLife {

  def next(g: Grid)(implicit log: EventLog): Option[Grid] = {
    val events = g.cells.map(GenerationService.nextGeneration(_).run(g)).filter(maybeEvent => maybeEvent.isDefined)

    events.foreach(e => e.foreach(log.add))

    log.add(TurnStarted(lastTurn(log) + 1))

    load(log)
  }

  private def lastTurn(log: EventLog): Int =
    (log
      .allEvents
      .filter { case TurnStarted(_) => true case _ => false }
      .map { case TurnStarted(t) => Some(t) case _ => None }:+Some(0)
      ).max.get


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
