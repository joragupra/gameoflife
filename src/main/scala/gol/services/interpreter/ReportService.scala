package gol.services.interpreter

import gol.model.{Alive, Cell, Dead, Grid}
import gol.services._

import scalaz._
import Scalaz._

class ReportServiceInterpreter extends ReportService[Grid, Cell] {

  override def generateCellReport(cell: Cell): CellReport = cell set List(get(cell))

  private def get(cell: Cell): String = cell match {
    case Cell(_, Alive) => "[*]"
    case Cell(_, Dead) => "[ ]"
  }

  override def generateRowReport(cells: List[Cell]): RowReport = cells.sortBy(_.coord.x)
    .foldLeft(cells set List[String]())((output, cell) => output :++> generateCellReport(cell).run._1)
    .swap.map(ls => List(ls mkString ""))
    .swap

  override def generateGridReport(grid: Grid): GridReport = {
    val rows = grid.cells.groupBy(_.coord.y)
    val rowReports = for (i <- rows.keys.toList.sorted) yield generateRowReport(rows(i))

    rowReports.foldLeft(grid set List[String]())((output, rowReport) => output :++> rowReport.run._1)
  }

}

object ReportService extends ReportServiceInterpreter
