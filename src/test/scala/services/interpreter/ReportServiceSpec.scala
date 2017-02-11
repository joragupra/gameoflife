package services.interpreter

import gol.model._
import gol.services.interpreter.ReportService
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class ReportServiceSpec extends FlatSpec {

  "A living cell" should "be reported as '[*]'" in {
    val livingCell = Cell(Coordinates(0, 0), Alive)

    ReportService.generateCellReport(livingCell).run._1 should be(List("[*]"))
  }

  "A dead cell" should "be reported as '[ ]'" in {
    val livingCell = Cell(Coordinates(0, 0), Dead)

    ReportService.generateCellReport(livingCell).run._1 should be(List("[ ]"))
  }

  "A list of cells" should "be reported as the concatenation of each cell's report" in {
    val cells = List(
      Cell(Coordinates(1, 0), Alive),
      Cell(Coordinates(0, 0), Dead),
      Cell(Coordinates(2, 0), Dead)
    )

    ReportService.generateRowReport(cells).run._1 should be(List("[ ][*][ ]"))
  }

  "A grid" should "be reported as a multiline report with each report as a row report" in {
    val grid = Grid(List(
      Cell(Coordinates(1, 0), Alive),
      Cell(Coordinates(0, 0), Dead),
      Cell(Coordinates(2, 0), Dead),
      Cell(Coordinates(1, 1), Alive),
      Cell(Coordinates(0, 1), Dead),
      Cell(Coordinates(2, 1), Alive),
      Cell(Coordinates(1, 2), Alive),
      Cell(Coordinates(0, 2), Dead),
      Cell(Coordinates(2, 2), Dead)
    ))

    ReportService.generateGridReport(grid).run._1 should be(List("[ ][*][ ]","[ ][*][*]","[ ][*][ ]"))
  }

}
