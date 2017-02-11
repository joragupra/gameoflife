package services.interpreter

import gol.model.{Alive, Cell, Coordinates, Dead}
import gol.services.interpreter.ReportService
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class ReportServiceSpec extends FlatSpec {

  "A living cell" should "be reported as '[*]'" in {
    val livingCell = Cell(Coordinates(0, 0), Alive)

    ReportService.get2(livingCell).run._1 should be("[*]")
  }

  "A dead cell" should "be reported as '[ ]'" in {
    val livingCell = Cell(Coordinates(0, 0), Dead)

    ReportService.get2(livingCell).run._1 should be("[ ]")
  }

  "A list of cells" should "be" in {

  }

}
