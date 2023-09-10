package mpdev.springboot.aoc2018.solutions.day13

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.Point
import mpdev.springboot.aoc2018.utils.plus

data class Car(var direction: CarDirection, var position: Point,
               var lastDirectionOfTurn: DirectionOfTurn = DirectionOfTurn.RIGHT, var crashed: Boolean = false) {

    fun moveToNextPoint(grid: Grid<TrackItem>) {
        val nextDirection: CarDirection
        when (grid.getDataPoint(position)) {
            TrackItem.STRAIGHT_V, TrackItem.STRAIGHT_H -> nextDirection = direction
            TrackItem.BEND1 ->
                nextDirection = when (direction) {
                    CarDirection.CAR_RIGHT, CarDirection.CAR_LEFT -> direction.turnLeft()
                    CarDirection.CAR_UP, CarDirection.CAR_DOWN -> direction.turnRight()
                }
            TrackItem.BEND2 ->
                nextDirection = when (direction) {
                    CarDirection.CAR_RIGHT, CarDirection.CAR_LEFT -> direction.turnRight()
                    CarDirection.CAR_UP, CarDirection.CAR_DOWN -> direction.turnLeft()
                }
            TrackItem.CROSS -> {
                val newDirectionOfTurn = lastDirectionOfTurn.getNext()
                nextDirection = when (newDirectionOfTurn) {
                    DirectionOfTurn.LEFT -> direction.turnLeft()
                    DirectionOfTurn.STRAIGHT -> direction
                    DirectionOfTurn.RIGHT -> direction.turnRight()
                }
                lastDirectionOfTurn = newDirectionOfTurn
            }
            else -> throw AocException("invalid position for direction $direction: $position ${grid.getDataPoint(position)}")
        }
        direction = nextDirection
        position += direction.speed
    }
}

enum class CarDirection(val value: TrackItem, val speed: Point) {
    CAR_UP(TrackItem.CAR_U, Point(0, -1)),
    CAR_RIGHT(TrackItem.CAR_R, Point(1, 0)),
    CAR_DOWN(TrackItem.CAR_D, Point(0, 1)),
    CAR_LEFT(TrackItem.CAR_L, Point(-1, 0));

    fun turnRight() = values()[ (ordinal+1) % CarDirection.values().size ]
    fun turnLeft() = values()[ (ordinal-1).mod(CarDirection.values().size) ]

    companion object {
        fun getDirectionFromValue(value: TrackItem): CarDirection =
            CarDirection.values().find { it.value == value } ?: throw AocException("could not encode car item for $value")
    }
}

enum class DirectionOfTurn {
    LEFT, STRAIGHT, RIGHT;
    fun getNext() = values()[ (ordinal+1) % values().size ]
}