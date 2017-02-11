package services.interpreter

import gol.model._
import gol.services.interpreter.LocationService
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class LocationServiceSpec extends FlatSpec {

  "Two coordinates" should "be neighbours if they are in the same y coordinates and the x-coordinates distance is one" in {
    val c1 = Coordinates(10, 10)
    val c2 = Coordinates(9, 10)

    LocationService.areNeighbours(c1, c2) should be(true)
  }

  they should "be neighbours if they are in the same x coordinates and the y-coordinates distance is one" in {
    val c1 = Coordinates(10, 10)
    val c2 = Coordinates(10, 11)

    LocationService.areNeighbours(c1, c2) should be(true)
  }

  they should "be neighbours if their x-coordinates and the y-coordinates distance is one" in {
    val c1 = Coordinates(10, 10)
    val c2 = Coordinates(9, 11)

    LocationService.areNeighbours(c1, c2) should be(true)
  }

  they should "not be neighbours if they are not in the same x coordinates nor in the same y coordinate and any of the distances is greater than one" in {
    val c1 = Coordinates(10, 10)
    val c2 = Coordinates(8, 5)

    LocationService.areNeighbours(c1, c2) should be(false)
  }

  val grid = Grid.withSize(0, 2)

  "A coordinate" should "have eight neighbours if it is an inner coordinate" in {
    val inner = Coordinates(1, 1)

    LocationService.neighbours(inner).run(grid).size should be(8)
  }

  it should "have five neighbours if it is in the border" in {
    val inBorder = Coordinates(1, 0)

    LocationService.neighbours(inBorder).run(grid).size should be(5)
  }

  it should "have three neighbours if it is in the corner" in {
    val inCorder = Coordinates(0, 0)

    LocationService.neighbours(inCorder).run(grid).size should be(3)
  }

}
