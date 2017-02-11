package gol.services.interpreter

import gol.model._
import gol.services._
import gol.services.interpreter.LocationService._

import scalaz.Reader

class GenerationServiceInterpreter extends GenerationService[Grid, Cell, Coordinates, State] {

  override def nextGeneration(cell: Cell): Reader[Grid, Option[Cell]] = {
    for {
      underpopulated <- underpopulation(cell)
      survivor <- survival(cell)
      overpopulated <- overpopulation(cell)
      reproduced <- reproduction(cell)
    } yield {
      underpopulated.orElse(survivor).orElse(overpopulated).orElse(reproduced).orElse(Some(cell.copy()))
    }
  }

  override def underpopulation(cell: Cell): Reader[Grid, Option[Cell]] = {
    countNeighbours(cell.coord, Alive).map(n => {
      if (cell.s == Alive && n < 2) Some(cell.copy(s = Dead))
      else None
    })
  }

  override def survival(cell: Cell): Reader[Grid, Option[Cell]] = {
    countNeighbours(cell.coord, Alive).map(n => {
      if (cell.s == Alive && (n == 2 || n == 3)) Some(cell.copy(s = Alive))
      else None
    })
  }

  override def overpopulation(cell: Cell): Reader[Grid, Option[Cell]] = {
    countNeighbours(cell.coord, Alive).map(n => {
      if (cell.s == Alive && n > 3) Some(cell.copy(s = Dead))
      else None
    })
  }

  override def reproduction(cell: Cell): Reader[Grid, Option[Cell]] = {
    countNeighbours(cell.coord, Alive).map(n => {
      if (cell.s == Dead && n == 3) Some(cell.copy(s = Alive))
      else None
    })
  }

  override def kill(coord: Coordinates)(implicit g: Grid): Grid = changeCellState(coord, Dead)

  override def resurrect(coord: Coordinates)(implicit g: Grid): Grid = changeCellState(coord, Alive)

  private def changeCellState(coord: Coordinates, s: State)(implicit g: Grid): Grid = {
    val otherCells = g.cells.filter(_.coord != coord)
    Grid(Cell(coord, s)::otherCells)
  }

}

object GenerationService extends GenerationServiceInterpreter
