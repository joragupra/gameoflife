package gol.services.interpreter

import gol.model.{Alive, Cell, Dead}
import gol.services._

import scalaz._
import Scalaz._

class ReportServiceInterpreter extends ReportService[Cell] {
  override def get(cells: PrintOutput, currentRow: Int): PrintOutput = {
    if (cells.run._2.isEmpty) cells
    else {
      val row = cells.run._2.filter(cell => cell.coord.y == currentRow)
      val remainingRows = cells.map(cells => cells.filter(cell => !row.contains(cell)))

      get(remainingRows :++> List(get(row)), currentRow + 1)
    }
  }

  override def get(cells: List[Cell]): String = cells.sortBy(_.coord.x).map(get) mkString " "

  override def get(cell: Cell): String = cell match {
    case Cell(_, Alive) => "[*]"
    case Cell(_, Dead) => "[ ]"
  }

  def get2(cell: Cell): Writer[String, Cell] = cell.set(get(cell))
}

object ReportService extends ReportServiceInterpreter
