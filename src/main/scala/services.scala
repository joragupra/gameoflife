import domain._
import services.{GenerationService, LocationService}

object services {

  trait GenerationService[Grid, Cell, Coordinates, State] {
    def next(implicit g: Grid): Option[Grid]

    def next(cell: Cell)(implicit g: Grid): Option[Cell]

    def kill(coord: Coordinates)(implicit g: Grid): Grid

    def resurrect(coord: Coordinates)(implicit g: Grid): Grid

  }

  trait LocationService[Coordinates] {
    def areNeighbours(c1: Coordinates, c2: Coordinates): Boolean

    def neighbours(coord: Coordinates, grid: Grid): List[Coordinates]
  }

}

class GOLInterpreter extends GenerationService[Grid, Cell, Coordinates, State] with LocationService[Coordinates] {
  import Math.abs

  override def areNeighbours(c1: Coordinates, c2: Coordinates): Boolean = (c1, c2) match {
    case (Coordinates(x1, y1), Coordinates(x2, y2)) if x1 == x2 => abs(y1 - y2) == 1
    case (Coordinates(x1, y1), Coordinates(x2, y2)) if y1 == y2 => abs(x1 - x2) == 1
    case (Coordinates(x1, y1), Coordinates(x2, y2)) => abs(x1 - x2) == 1 && abs(y1 - y2) == 1
    case (_, _) => false
  }

  override def neighbours(coord: Coordinates, grid: Grid): List[Coordinates] =
    grid.cells.map(_.coord).filter(areNeighbours(coord, _))

  override def next(cell: Cell)(implicit g: Grid): Option[Cell] =
    underpopulation(cell).orElse(survival(cell)).orElse(overpopulation(cell)).orElse(reproduction(cell)).orElse(Some(cell))

  private def underpopulation(cell: Cell)(implicit g: Grid): Option[Cell] = {
    val neighbouringCells = g.cells.filter(c => neighbours(cell.coord, g).contains(c.coord))
    if (cell.s == Alive && neighbouringCells.count(_.s == Alive) < 2) Some(Cell(cell.coord, Dead))
    else None
  }

  private def survival(cell: Cell)(implicit g: Grid): Option[Cell] = {
    val neighbouringCells = g.cells.filter(c => neighbours(cell.coord, g).contains(c.coord))
    if (cell.s == Alive && (neighbouringCells.count(_.s == Alive) == 2 || neighbouringCells.count(_.s == Alive) == 3)) Some(Cell(cell.coord, Alive))
    else None
  }

  private def overpopulation(cell: Cell)(implicit g: Grid): Option[Cell] = {
    val neighbouringCells = g.cells.filter(c => neighbours(cell.coord, g).contains(c.coord))
    if (cell.s == Alive && neighbouringCells.count(_.s == Alive) > 3) Some(Cell(cell.coord, Dead))
    else None
  }

  private def reproduction(cell: Cell)(implicit g: Grid): Option[Cell] = {
    val neighbouringCells = g.cells.filter(c => neighbours(cell.coord, g).contains(c.coord))
    if (cell.s == Dead && neighbouringCells.count(_.s == Alive) == 3) Some(Cell(cell.coord, Alive))
    else None
  }

  override def kill(coord: Coordinates)(implicit g: Grid): Grid = changeCellState(coord, Dead)

  override def resurrect(coord: Coordinates)(implicit g: Grid): Grid = changeCellState(coord, Alive)

  private def changeCellState(coord: Coordinates, s: State)(implicit g: Grid): Grid = {
    val otherCells = g.cells.filter(_.coord != coord)
    Grid(Cell(coord, s)::otherCells)
  }

  override def next(implicit g: Grid): Option[Grid] = Some(Grid(g.cells.map(next(_).get)))

}

object GameOfLife extends GOLInterpreter

