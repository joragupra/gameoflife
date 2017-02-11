package gol.services

import scalaz.Writer

trait ReportService[Grid, Cell] {
  type GridReport  = Writer[List[String], Grid]

  type RowReport = Writer[List[String], List[Cell]]

  type CellReport = Writer[List[String], Cell]

  def generateCellReport(cell: Cell): CellReport

  def generateRowReport(cells: List[Cell]): RowReport

  def generateGridReport(grid: Grid): GridReport

}
