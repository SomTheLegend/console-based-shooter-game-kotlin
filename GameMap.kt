class GameMap(val width: Int, val height: Int) {

    private val grid: Array<CharArray> = Array(height)
    { CharArray(width) { EMPTY } }

    companion object {
        const val EMPTY = '.'
        const val OBSTACLE = '#'
        const val HEALTH_PACK = 'H'
        const val AMMO_PACK = 'A'
    }

    fun placeObject(x: Int, y: Int, objectChar: Char) {
        if (isValid(x, y)) {
            grid[y][x] = objectChar
        }
    }

    fun isValid(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }

    fun getObjectAt(x: Int, y: Int): Char {
        return if (isValid(x, y)) grid[y][x] else '\u0000'
    }

    fun isOccupiedByObstacle(x: Int, y: Int): Boolean {
        return isValid(x, y) && grid[y][x] == OBSTACLE
    }

    fun hasLineOfSight(start: Point, end: Point): Boolean {

        var x1 = start.x
        var y1 = start.y
        var x2 = end.x
        var y2 = end.y

        val dx = Math.abs(x2 - x1)
        val dy = Math.abs(y2 - y1)
        val sx = if (x1 < x2) 1 else -1
        val sy = if (y1 < y2) 1 else -1
        var err = dx - dy

        while (true) {
            if (x1 != start.x || y1 != start.y) {
                if (isOccupiedByObstacle(x1, y1)) {
                    return false
                }
            }
            if (x1 == x2 && y1 == y2) {
                break
            }

            val e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                x1 += sx
            }
            if (e2 < dx) {
                err += dx
                y1 += sy
            }
        }
        return true
    }

    fun display(combatants: List<Combatant>) {

        val displayGrid = grid.map { it.clone() }.toTypedArray()

        combatants.filter { it.isAlive }.forEach { c ->
            val teamChar = when {
                c is Player -> 'P'
                c.team == Team.ALPHA -> 'A'
                else -> 'B'
            }
            displayGrid[c.position.y][c.position.x] = teamChar
        }

        println("\n--- MAP ---")
        displayGrid.forEach { row ->
            println(row.joinToString(" "))
        }

        println("P: Player | A: Alpha Team | B: Bravo Team " +
        "| H: Health Pack | A: Ammo Pack | #: Obstacle")
    }
}
