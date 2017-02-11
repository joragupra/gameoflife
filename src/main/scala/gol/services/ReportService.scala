package gol.services

import scalaz.Writer

trait ReportService[Cell] {
  type PrintOutput = Writer[List[String], List[Cell]]

  def get(cells: PrintOutput, currentRow: Int): PrintOutput

  def get(cells: List[Cell]): String

  def get(cell: Cell): String

}
