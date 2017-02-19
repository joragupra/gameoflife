package gol.services.interpreter

import gol.model._
import gol.services.GridService

import scalaz.{-\/, NonEmptyList, \/, \/-}

class GridServiceInterpreter extends GridService[Grid, Coordinates, TurnEvent] {

  override def loadGrid(events: Seq[TurnEvent]): \/[NonEmptyList[String], Grid] = loadGrid(events, \/-(Grid(List())))

  private def loadGrid(events: Seq[TurnEvent], acc: \/[NonEmptyList[String], Grid]): \/[NonEmptyList[String], Grid] = {
    events match {
      case Nil => acc
      case event::rest => {
        for {
          currentGrid <- acc
          updatedGrid <- compute(currentGrid, event)
          fullyProcessedGrid <- loadGrid(rest, \/-(updatedGrid))
        } yield {
          fullyProcessedGrid
        }
      }
    }
  }

  private def compute(g: Grid, event: TurnEvent): \/[NonEmptyList[String], Grid] = {
    event match {
      case coord: CoordinateEnabled => enableCoordinate(coord.c, g)
      case resurr: CellResurrected => resurrectCell(resurr.c, g)
      case died: CellDied => killCell(died.c, g)
      case _: TurnStarted => \/-(g)
      case e => -\/(NonEmptyList("Event not recognized: " + e))
    }
  }

  private def enableCoordinate(coord: Coordinates, g: Grid): \/[NonEmptyList[String], Grid] = {
    if (LocationService.find(coord).run(g).isDefined) -\/(NonEmptyList("Coordinate " + coord + " cannot be enabled more than once"))
    else \/-(Grid(Cell(coord.copy(), Dead)::g.cells))
  }

  private def resurrectCell(coord: Coordinates, g: Grid): \/[NonEmptyList[String], Grid] = changeCellState(coord, Alive, g)

  private def killCell(coord: Coordinates, g: Grid): \/[NonEmptyList[String], Grid] = changeCellState(coord, Dead, g)

  private def changeCellState(coord: Coordinates, s: State, g: Grid): \/[NonEmptyList[String], Grid] = {
    val currentCell = LocationService.find(coord).run(g)
    val errorMsg = "Cell at coordinate " + coord + " cannot " + (if (s == Alive) "be resurrected" else "die")

    if (currentCell.isEmpty) -\/(NonEmptyList(errorMsg + ": not found"))
    else if (currentCell.get.s == s) -\/(NonEmptyList(errorMsg + ": already in that state"))
    else \/-(Grid(currentCell.get.copy(s = s)::g.cells.filter(cell => cell.coord != coord)))
  }

  override def createGrid(x: Int, y: Int, initiallyAliveCellsAt: Seq[Coordinates]): Seq[TurnEvent] = {
    val initialEvents: Seq[TurnEvent] = for {
      i <- 0 until x
      j <- 0 until y
    } yield CoordinateEnabled(Coordinates(i,j))

    val aliveCellEvents = initiallyAliveCellsAt.map(coord => CellResurrected(coord))

    initialEvents++:aliveCellEvents
  }
}

object GridService extends GridServiceInterpreter
