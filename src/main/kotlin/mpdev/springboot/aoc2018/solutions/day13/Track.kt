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

    fun runCarsUntilFirstCrash(debug: Boolean = false): Point {
        repeat (100_000) { count ->
            for (car in cars) {
                car.moveToNextPoint(grid)
                val collisionMap = cars.groupBy { it.position }.filter { it.value.count() > 1 }
                if (collisionMap.isNotEmpty())
                    return collisionMap.keys.first()
            }
            cars.sortBy { car -> car.position }
            if (debug) {
                println("iteration ${count+1}"); print(); println(cars)
            }
        }
        return Point(-1,-1)
    }

    fun runCarsAndRemoveCrashes(debug: Boolean = false): Point {
        repeat (1_000_000) { count ->
            if (cars.size > 1) {
                cars.forEach { car ->
                    if (car.crashed)
                        return@forEach
                    car.moveToNextPoint(grid)
                    val collisionMap = cars.groupBy { it.position }.filter { it.value.count() > 1 }
                    if (collisionMap.isNotEmpty())
                        cars.filter { collisionMap.keys.contains(it.position) }.forEach { it.crashed = true }
                }
                cars = cars.filterNot { it.crashed }.toMutableList()
                cars.sortBy { car -> car.position }
                if (debug) {
                    println("iteration ${count+1}"); print(); println(cars)
                }
            }
            else
                return cars.first().position
        }
        return Point(-1,-1)
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
