package mpdev.springboot.aoc2018.solutions.day13

import mpdev.springboot.aoc2018.utils.*

class Track(val input: List<String>) {

    val grid = Grid(input, TrackItem.mapper, defaultChar = ' ', border = 0)
    var cars = mutableListOf<Car>()

    init {
        grid.getDataPoints().filter { TrackItem.isCar(it.value) }.forEach {
            cars.add(Car(CarDirection.getDirectionFromValue(it.value), it.key))
            grid.setDataPoint(it.key, if (setOf(TrackItem.CAR_U, TrackItem.CAR_D).contains(it.value))
                    TrackItem.STRAIGHT_V
                else
                    TrackItem.STRAIGHT_H
            )
        }
        cars.sortBy { it.position }
    }

    fun runCars(debug: Boolean = false): Point {
        repeat (100_000) {
            for (i in cars.indices) {
                val (nextPos, newDir) = getNextPoint(cars[i])
                if (cars.any{ it.position == nextPos })
                    return nextPos
                cars[i].position = nextPos
                cars[i].direction = newDir
            }
            cars.sortBy { car -> car.position }
            if (debug) {
                println("iteration $it")
                print()
                println(cars)
            }
        }
        return Point(-1,-1)
    }

    private fun getNextPoint(car: Car): Pair<Point,CarDirection> {
        val position = car.position
        val direction = car.direction
        val lastDirectionOfTurn = car.lastDirectionOfTurn
        val nextDirection: CarDirection
        when (direction) {
            CarDirection.CAR_UP -> {
                when (grid.getDataPoint(position)) {
                    TrackItem.STRAIGHT_V -> nextDirection = direction
                    TrackItem.BEND1 -> nextDirection = CarDirection.CAR_RIGHT
                    TrackItem.BEND2 -> nextDirection = CarDirection.CAR_LEFT
                    TrackItem.CROSS -> {
                        when (lastDirectionOfTurn.getNext()) {
                            DirectionOfTurn.LEFT -> nextDirection = CarDirection.CAR_LEFT
                            DirectionOfTurn.STRAIGHT -> nextDirection = CarDirection.CAR_UP
                            DirectionOfTurn.RIGHT -> nextDirection = CarDirection.CAR_RIGHT
                        }
                        car.lastDirectionOfTurn = lastDirectionOfTurn.getNext()
                    }
                    else -> throw AocException("invalid position for direction $direction: $position ${grid.getDataPoint(position)}")
                }
            }
            CarDirection.CAR_RIGHT -> {
                when (grid.getDataPoint(position)) {
                    TrackItem.STRAIGHT_H -> nextDirection = direction
                    TrackItem.BEND1 -> nextDirection = CarDirection.CAR_UP
                    TrackItem.BEND2 -> nextDirection = CarDirection.CAR_DOWN
                    TrackItem.CROSS -> {
                        when (lastDirectionOfTurn.getNext()) {
                            DirectionOfTurn.LEFT -> nextDirection = CarDirection.CAR_UP
                            DirectionOfTurn.STRAIGHT -> nextDirection = CarDirection.CAR_RIGHT
                            DirectionOfTurn.RIGHT -> nextDirection = CarDirection.CAR_DOWN
                        }
                        car.lastDirectionOfTurn = lastDirectionOfTurn.getNext()
                    }
                    else -> throw AocException("invalid position for direction $direction: $position ${grid.getDataPoint(position)}")
                }
            }
            CarDirection.CAR_DOWN -> {
                when (grid.getDataPoint(position)) {
                    TrackItem.STRAIGHT_V -> nextDirection = direction
                    TrackItem.BEND1 -> nextDirection = CarDirection.CAR_LEFT
                    TrackItem.BEND2 -> nextDirection = CarDirection.CAR_RIGHT
                    TrackItem.CROSS -> {
                        when (lastDirectionOfTurn.getNext()) {
                            DirectionOfTurn.LEFT -> nextDirection = CarDirection.CAR_RIGHT
                            DirectionOfTurn.STRAIGHT -> nextDirection = CarDirection.CAR_DOWN
                            DirectionOfTurn.RIGHT -> nextDirection = CarDirection.CAR_LEFT
                        }
                        car.lastDirectionOfTurn = lastDirectionOfTurn.getNext()
                    }
                    else -> throw AocException("invalid position for direction $direction: $position ${grid.getDataPoint(position)}")
                }
            }
            CarDirection.CAR_LEFT -> {
                when (grid.getDataPoint(position)) {
                    TrackItem.STRAIGHT_H -> nextDirection = direction
                    TrackItem.BEND1 -> nextDirection = CarDirection.CAR_DOWN
                    TrackItem.BEND2 -> nextDirection = CarDirection.CAR_UP
                    TrackItem.CROSS -> {
                        when (lastDirectionOfTurn.getNext()) {
                            DirectionOfTurn.LEFT -> nextDirection = CarDirection.CAR_DOWN
                            DirectionOfTurn.STRAIGHT -> nextDirection = CarDirection.CAR_LEFT
                            DirectionOfTurn.RIGHT -> nextDirection = CarDirection.CAR_UP
                        }
                        car.lastDirectionOfTurn = lastDirectionOfTurn.getNext()
                    }
                    else -> throw AocException("invalid position for direction $direction: $position ${grid.getDataPoint(position)}")
                }
            }
        }
        return Pair(position + nextDirection.speed, nextDirection)
    }

    fun print() {
        val gridToPrint = Grid(grid.getDataPoints(), TrackItem.mapper, defaultChar = ' ', border = 0)
        cars.forEach { gridToPrint.setDataPoint(it.position, it.direction.value) }
        gridToPrint.print()
    }

}

enum class TrackItem(val value: Char) {
    STRAIGHT_V('|'),
    STRAIGHT_H('-'),
    CROSS('+'),
    BEND1('/'),
    BEND2('\\'),
    CAR_U('^'),
    CAR_R('>'),
    CAR_D('v'),
    CAR_L('<');

    companion object {
        val mapper: Map<Char, TrackItem> = TrackItem.values().associateBy { it.value }
        fun isCar(item: TrackItem) = setOf(CAR_U,CAR_R,CAR_D,CAR_L).contains(item)
    }
}

data class Car(var direction: CarDirection, var position: Point) {
    var lastDirectionOfTurn = DirectionOfTurn.RIGHT
}

enum class CarDirection(val value: TrackItem, val speed: Point) {
    CAR_UP(TrackItem.CAR_U, Point(0, -1)),
    CAR_RIGHT(TrackItem.CAR_R, Point(1, 0)),
    CAR_DOWN(TrackItem.CAR_D, Point(0, 1)),
    CAR_LEFT(TrackItem.CAR_L, Point(-1, 0));

    companion object {
        fun getDirectionFromValue(value: TrackItem): CarDirection =
            CarDirection.values().find { it.value == value } ?: throw AocException("could not encode car item for $value")
    }
}

enum class DirectionOfTurn {
    LEFT, STRAIGHT, RIGHT;
    fun getNext() = values()[ (ordinal+1) % values().size ]
}