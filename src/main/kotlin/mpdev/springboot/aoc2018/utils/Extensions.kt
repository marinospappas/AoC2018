package mpdev.springboot.aoc2018.utils

import java.awt.Point
import java.lang.StringBuilder
import kotlin.math.abs

operator fun Point.plus(other: Point) =
    Point(this.x + other.x, this.y + other.y)

operator fun Point.times(n: Int) =
    Point(n * this.x, n * this.y)

operator fun Point.minus(other: Point) =
    Point(this.x - other.x, this.y - other.y)

fun Point.adjacent(): Array<Point> =
    listOf(Point(-1,0), Point(-1,-1), Point(0,-1), Point(1,-1),
        Point(1,0), Point(1,1), Point(0,1), Point(-1,1))
        .map { this + it }
        .toTypedArray()

fun Point.manhattan(other: Point): Int =
    abs(this.x - other.x) + abs(this.y - other.y)

fun String.splitRepeatedChars(): List<String> {
    if (isEmpty())
        return emptyList()
    val s = StringBuilder(this)
    var index = 0
    val delimiter = '_'
    var previous = s.first()
    while (index < s.length) {
        if (s[index] != previous)
            s.insert(index++, delimiter)
        previous = s[index]
        ++index
    }
    return s.split(delimiter)
}

fun Int.lastDigit() = this % 10

operator fun IntArray.plus(other: IntArray) = Array(size) { this[it] + other[it] }.toIntArray()

fun Array<IntRange>.allValues(): Set<MutableList<Int>> {
    return if (size == 1)
        (0 .. this[0].last - this[0].first).map { mutableListOf(this[0].first + it) }.toSet()
    else {
        val result = mutableSetOf<MutableList<Int>>()
        this.last().forEach { value ->
            this.sliceArray(0 .. size - 2).allValues().forEach { combo ->
                result.add(combo.also { combo.add(value) })
            }
        }
        result
    }
}