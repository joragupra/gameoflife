import gol.model.Grid
import gol.services.interpreter.GenerationService

object GameOfLife {

  def next(g: Grid): Option[Grid] = Some(Grid(g.cells.map(GenerationService.nextGeneration(_).run(g).get)))

}
