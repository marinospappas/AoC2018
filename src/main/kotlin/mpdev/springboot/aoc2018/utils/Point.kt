package mpdev.springboot.aoc2018.utils

data class Point(var x: Int, var y: Int): Comparable<Point> {
    constructor(p: Point): this(p.x, p.y)
    override fun compareTo(other: Point): Int {
        // order is from top to bottom and from left to right
        // with y increasing downwards and x increasing to the right
        if (this.y == other.y)
            return this.x.compareTo(other.x)
        else
            return this.y.compareTo(other.y)
    }
    override fun toString() = "[$x,$y]"
}