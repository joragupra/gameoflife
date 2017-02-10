import domain.{Cell, Coordinates, Grid, State, _}
import services.{GenerationService, LocationService, PrintService}

import scalaz._
import Scalaz._

object services {

  trait PrintService[Grid] {
    def printGrid(g: Grid): Unit
  }

  trait GenerationService[Grid, Cell, Coordinates, State] {
    def kill(coord: Coordinates)(implicit g: Grid): Grid

    def resurrect(coord: Coordinates)(implicit g: Grid): Grid

    def nextGeneration(cell:Cell): Reader[Grid, Option[Cell]]

    def underpopulation(cell: Cell): Reader[Grid, Option[Cell]]

    def survival(cell: Cell): Reader[Grid, Option[Cell]]

    def overpopulation(cell: Cell): Reader[Grid, Option[Cell]]

    def reproduction(cell: Cell): Reader[Grid, Option[Cell]]
  }

  trait LocationService[Coordinates, State] {
    def areNeighbours(c1: Coordinates, c2: Coordinates): Boolean

    def neighbours(coord: Coordinates): Reader[Grid, List[Cell]]

    def countNeighbours(coord: Coordinates, s: State): Reader[Grid, Int]
  }
}

class LocationServiceInterpreter extends LocationService[Coordinates, State] {
  import Math.abs

  override def areNeighbours(c1: Coordinates, c2: Coordinates): Boolean = (c1, c2) match {
    case (Coordinates(x1, y1), Coordinates(x2, y2)) if x1 == x2 => abs(y1 - y2) == 1
    case (Coordinates(x1, y1), Coordinates(x2, y2)) if y1 == y2 => abs(x1 - x2) == 1
    case (Coordinates(x1, y1), Coordinates(x2, y2)) => abs(x1 - x2) == 1 && abs(y1 - y2) == 1
    case (_, _) => false
  }

  override def neighbours(coord: Coordinates): Reader[Grid, List[Cell]] =
    Reader {
      (g: Grid) => g.cells.filter(cell => areNeighbours(cell.coord, coord))
    }

  override def countNeighbours(coord: Coordinates, state: State): Reader[Grid, Int] = neighbours(coord).map(cells => cells.count(_.s == state))
}

class GenerationServiceInterpreter extends GenerationService[Grid, Cell, Coordinates, State] {
  import LocationService._

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

class PrintServiceInterpreter extends PrintService[Grid] {
  type PrintOutput = Writer[List[String], List[Cell]]

  override def printGrid(g: Grid): Unit = {
    val output = print(g.cells.set(Nil), 0)
    output.run._1.foreach(s => println(s))
  }

  private def print(cells: PrintOutput, currentRow: Int): PrintOutput = {
    if (cells.run._2.isEmpty) cells
    else {
      val row = cells.run._2.filter(cell => cell.coord.y == currentRow)
      val remainingRows = cells.map(cells => cells.filter(cell => !row.contains(cell)))

      print(remainingRows :++> List(get(row)), currentRow + 1)
    }
  }

  private def get(cells: List[Cell]): String = cells.sortBy(_.coord.x).map(get) mkString " "

  private def get(cell: Cell): String = cell match {
    case Cell(_, Alive) => "[*]"
    case Cell(_, Dead) => "[ ]"
  }

}

object LocationService extends LocationServiceInterpreter

object GenerationService extends GenerationServiceInterpreter

object PrintService extends PrintServiceInterpreter