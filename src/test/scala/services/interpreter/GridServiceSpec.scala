package services.interpreter

import gol.model._
import gol.services.interpreter.GridService
import org.scalatest.FlatSpec
import org.scalatest.Matchers._


class GridServiceSpec extends FlatSpec {

  "A grid" should "be constructed only with dead cells when coordinates are enabled" in {
    val events = List(
      CoordinateEnabled(Coordinates(0, 0)),
      CoordinateEnabled(Coordinates(0, 1)),
      CoordinateEnabled(Coordinates(1, 0)),
      CoordinateEnabled(Coordinates(1, 1))
    )

    val grid = GridService.loadGrid(events)

    grid.isRight should be(true)
    grid.foreach(
      g => {
        g.cells.size should be(4)
        g.cells.forall(cell => cell.s == Dead) should be(true)
      }
    )
  }

  "A coordinate" should "not be enabled more than once" in {
    val repeatedCoordinate = Coordinates(0, 1)
    val events = List(
      CoordinateEnabled(Coordinates(0, 0)),
      CoordinateEnabled(repeatedCoordinate),
      CoordinateEnabled(Coordinates(1, 0)),
      CoordinateEnabled(Coordinates(1, 1)),
      CoordinateEnabled(repeatedCoordinate)
    )

    val error = GridService.loadGrid(events)

    error.isLeft should be(true)
    error.swap.foreach{
      msg => msg.head should be(s"Coordinate " + repeatedCoordinate + " cannot be enabled more than once")
    }
  }

  "A cell" should "be alive after it was resurrected" in {
    val events = List(
      CoordinateEnabled(Coordinates(0, 0)),
      CoordinateEnabled(Coordinates(0, 1)),
      CoordinateEnabled(Coordinates(1, 0)),
      CoordinateEnabled(Coordinates(1, 1)),
      CellResurrected(Coordinates(1, 0))
    )

    val grid = GridService.loadGrid(events)

    grid.isRight should be(true)
    grid.map(g => g.cells.find(_.coord == Coordinates(1, 0))).foreach{
      cell => cell.get.s should be(Alive)
    }
  }

  "A cell" should "not be resurrected when it is already alive" in {
    val events = List(
      CoordinateEnabled(Coordinates(0, 0)),
      CoordinateEnabled(Coordinates(0, 1)),
      CoordinateEnabled(Coordinates(1, 0)),
      CoordinateEnabled(Coordinates(1, 1)),
      CellResurrected(Coordinates(1, 0)),
      CellResurrected(Coordinates(1, 0))
    )

    val error = GridService.loadGrid(events)

    error.isLeft should be(true)
    error.swap.foreach{
      msg => msg.head should be(s"Cell at coordinate " + Coordinates(1, 0) + " cannot be resurrected: already in that state")
    }
  }

  "A cell" should "not be resurrected when it does not exist in the grid" in {
    val events = List(
      CoordinateEnabled(Coordinates(0, 0)),
      CoordinateEnabled(Coordinates(0, 1)),
      CoordinateEnabled(Coordinates(1, 0)),
      CoordinateEnabled(Coordinates(1, 1)),
      CellResurrected(Coordinates(3, 3))
    )

    val error = GridService.loadGrid(events)

    error.isLeft should be(true)
    error.swap.foreach{
      msg => msg.head should be(s"Cell at coordinate " + Coordinates(3, 3) + " cannot be resurrected: not found")
    }
  }














  "A cell" should "be dead after it was resurrected" in {
    val events = List(
      CoordinateEnabled(Coordinates(0, 0)),
      CoordinateEnabled(Coordinates(0, 1)),
      CoordinateEnabled(Coordinates(1, 0)),
      CoordinateEnabled(Coordinates(1, 1)),
      CellResurrected(Coordinates(1, 0)),
      CellDied(Coordinates(1, 0))
    )

    val grid = GridService.loadGrid(events)

    grid.isRight should be(true)
    grid.map(g => g.cells.find(_.coord == Coordinates(1, 0))).foreach{
      cell => cell.get.s should be(Dead)
    }
  }

  "A cell" should "not die when it is already dead" in {
    val events = List(
      CoordinateEnabled(Coordinates(0, 0)),
      CoordinateEnabled(Coordinates(0, 1)),
      CoordinateEnabled(Coordinates(1, 0)),
      CoordinateEnabled(Coordinates(1, 1)),
      CellResurrected(Coordinates(1, 0)),
      CellDied(Coordinates(1, 0)),
      CellDied(Coordinates(1, 0))
    )

    val error = GridService.loadGrid(events)

    error.isLeft should be(true)
    error.swap.foreach{
      msg => msg.head should be(s"Cell at coordinate " + Coordinates(1, 0) + " cannot die: already in that state")
    }
  }

  "A cell" should "not die when it does not exist in the grid" in {
    val events = List(
      CoordinateEnabled(Coordinates(0, 0)),
      CoordinateEnabled(Coordinates(0, 1)),
      CoordinateEnabled(Coordinates(1, 0)),
      CoordinateEnabled(Coordinates(1, 1)),
      CellResurrected(Coordinates(1, 0)),
      CellDied(Coordinates(3, 3))
    )

    val error = GridService.loadGrid(events)

    error.isLeft should be(true)
    error.swap.foreach{
      msg => msg.head should be(s"Cell at coordinate " + Coordinates(3, 3) + " cannot die: not found")
    }
  }

  "An error" should "be raised when an unknown event is provided" in {
    val events = List(
      CoordinateEnabled(Coordinates(0, 0)),
      CoordinateEnabled(Coordinates(0, 1)),
      CoordinateEnabled(Coordinates(1, 0)),
      CoordinateEnabled(Coordinates(1, 1)),
      TurnStarted(1)
    )

    val error = GridService.loadGrid(events)

    error.isLeft should be(true)
    error.swap.foreach{
      msg => msg.head should be("Event not recognized: " + TurnStarted(1))
    }
  }

  "A grid" should "be created with all cells dead when no alive cell is provided" in {
    val grid = GridService.createGrid(10, 20, Seq.empty)

    grid.size should be(200)
    grid.forall { case CoordinateEnabled(_) => true case _ => false } should be(true)
  }

  "A grid" should "be created with the specified cells alive" in {
    val grid = GridService.createGrid(10, 20, Seq(Coordinates(0,0), Coordinates(2, 3), Coordinates(5, 8)))

    grid.size should be(203)
    grid.count { case CoordinateEnabled(_) => true case _ => false } should be(200)
    grid.count { case CellResurrected(_) => true case _ => false } should be(3)
    grid.contains(CellResurrected(Coordinates(0,0))) should be(true)
    grid.contains(CellResurrected(Coordinates(2, 3))) should be(true)
    grid.contains(CellResurrected(Coordinates(5, 8))) should be(true)
  }

}
