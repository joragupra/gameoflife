package gol.services.interpreter

import gol.model._
import gol.services._
import gol.services.interpreter.LocationService._

import scalaz.Reader

class GenerationServiceInterpreter extends GenerationService[Grid, Cell, Coordinates, State, TurnEvent] {

  override def nextGeneration(cell: Cell): Reader[Grid, Option[TurnEvent]] = {
    for {
      underpopulated <- underpopulation(cell)
      survivor <- survival(cell)
      overpopulated <- overpopulation(cell)
      reproduced <- reproduction(cell)
    } yield {
      underpopulated.orElse(survivor).orElse(overpopulated).orElse(reproduced).orElse(None)
    }
  }

  override def underpopulation(cell: Cell): Reader[Grid, Option[TurnEvent]] = {
    countNeighbours(cell.coord, Alive).map(n => {
      if (cell.s == Alive && n < 2) Some(CellDied(cell.coord))
      else None
    })
  }

  override def survival(cell: Cell): Reader[Grid, Option[TurnEvent]] = Reader{ g => None}

  override def overpopulation(cell: Cell): Reader[Grid, Option[TurnEvent]] = {
    countNeighbours(cell.coord, Alive).map(n => {
      if (cell.s == Alive && n > 3) Some(CellDied(cell.coord))
      else None
    })
  }

  override def reproduction(cell: Cell): Reader[Grid, Option[TurnEvent]] = {
    countNeighbours(cell.coord, Alive).map(n => {
      if (cell.s == Dead && n == 3) Some(CellResurrected(cell.coord))
      else None
    })
  }

}

object GenerationService extends GenerationServiceInterpreter
