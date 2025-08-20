abstract class Combatant (

    val name: String,
    val maxHealth: Int,
    val team: Team,
    var currentWeapon: Weapon
) {

    var health: Int = maxHealth
        private set
    var inCover: Boolean = false
    val position: Point = Point()
    val isAlive: Boolean
        get() = health > 0

    fun setPosition(x: Int, y: Int) {
        position.setLocation(x, y)
    }

    fun moveTowards(target: Point, game: Game) {

        val map = game.map
        val currentX = position.x
        val currentY = position.y

        val dx = Integer.compare(target.x, currentX)
        val dy = Integer.compare(target.y, currentY)

        val newX = currentX + dx
        val newY = currentY + dy

        if (dx != 0 && !map.isOccupiedByObstacle(newX, currentY) && !game.isTileOccupiedByCombatant(newX, currentY)) {
            setPosition(newX, currentY)
       }
        else if (dy != 0 && !map.isOccupiedByObstacle(currentX, newY) && !game.isTileOccupiedByCombatant(currentX, newY)) {
            setPosition(currentX, newY)
       }

        println("$name moved to (${position.x}, ${position.y})")
    }

    fun takeDamage(damage: Int) {

        val damageTaken = if (inCover) {
            println("$name is in cover and takes reduced damage!")
            damage / 2
        } else {
            damage
        }

        health -= damageTaken
        if (health < 0) health = 0
        println("$name took $damageTaken damage. Health is now $health")

        if (inCover) {
            inCover = false
            println("$name is no longer in cover.")
        }

        if (!isAlive) {
            println("$name has been defeated!")
        }
    }

    fun takeCover() {
        inCover = true
        println("$name gets into cover!")
    }

    fun heal(amount: Int) {
        health = (health + amount).coerceAtMost(maxHealth)
        println("$name healed for $amount. Health is now $health")
    }

    abstract fun performAction(game: Game)
}
