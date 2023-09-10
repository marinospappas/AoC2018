package mpdev.springboot.aoc2018.solutions.day13

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.Point
import mpdev.springboot.aoc2018.utils.plus

data class Car(var direction: CarDirection, var position: Point, var lastDirectionOfTurn: DirectionOfTurn = DirectionOfTurn.RIGHT) {

    fun moveToNextPoint(grid: Grid<TrackItem>) {
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
                        lastDirectionOfTurn = lastDirectionOfTurn.getNext()
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
                        lastDirectionOfTurn = lastDirectionOfTurn.getNext()
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
                        lastDirectionOfTurn = lastDirectionOfTurn.getNext()
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
                        lastDirectionOfTurn = lastDirectionOfTurn.getNext()
                    }
                    else -> throw AocException("invalid position for direction $direction: $position ${grid.getDataPoint(position)}")
                }
            }
        }
        position += nextDirection.speed
        direction = nextDirection
    }
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