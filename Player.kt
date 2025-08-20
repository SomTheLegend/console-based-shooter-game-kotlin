class Player(
    name: String,
    health: Int,
    team: Team,
    startingWeapon: Weapon
) : Combatant(name, health, team, startingWeapon){

    override fun performAction(game: Game) {

        if (!isAlive) return

        if (inCover) {
            inCover = false
            println("You must leave cover to make your move.")
        }

        println("\n--- YOUR TURN, $name ---")
        println("Position: (${position.x}, ${position.y}) | Health: $health")
        println("Weapon: ${currentWeapon.name} | Ammo: ${currentWeapon.currentAmmo}")

        println("Choose an option:")
        println("1. Move")
        println("2. Attack")
        println("3. Take Cover")
        println("4. Reload")

        when (getPlayerInput(1, 4)) {
            1 -> handleMove(game)
            2 -> handleAttack(game)
            3 -> takeCover()
            4 -> currentWeapon.reload()
        }
    }

    private fun handleMove(game: Game) {

        val map = game.map
        println("Enter target X coordinate: ")
        val targetX = getPlayerInput(0, map.width - 1)

        println("Enter target Y coordinate: ")
        val targetY = getPlayerInput(0, map.height - 1)

        if (map.isOccupiedByObstacle(targetX, targetY)) {
            println("Cannot move to that location! There is an obstacle.")
        } else if (game.isTileOccupiedByCombatant(targetX, targetY)) {
            println("Cannot move to that location! It is occupied.")
        }
        else {
            setPosition(targetX, targetY)
            println("$name moves to (${targetX}, ${targetY}).")
        }
    }

    private fun handleAttack(game: Game) {

        val validTargets = game.allCombatants.filter {
            it.isAlive && it.team != this.team &&
                    game.map.hasLineOfSight(position, it.position, game.allCombatants)
        }

        if (validTargets.isEmpty()) {
            println("No enemies in line of sight!")
            return
        }

        if (!currentWeapon.hasAmmo()) {
            println("${currentWeapon.name} is out of ammo! You must reload")
            return
        }

        println("Choose a target:")
        validTargets.forEachIndexed { index, target ->
            println("${index + 1}. ${target.name} at " +
                    "(${target.position.x}, ${target.position.y})")
        }

        val targetIndex = getPlayerInput(1, validTargets.size) - 1
        val chosenTarget = validTargets[targetIndex]

        println("$name attacks ${chosenTarget.name} with ${currentWeapon.name}!")
        currentWeapon.useAmmo()
        chosenTarget.takeDamage(currentWeapon.damage)
    }

    private fun getPlayerInput(min: Int, max: Int): Int {

        while (true) {
            print("> ")
            try {
                val choice = readLine()!!.toInt()
                if (choice in min..max) {
                    return choice
                } else {
                    println("Invalid choice. Please enter a number between $min and $max.")
                }
            }
            catch (e: NumberFormatException) {
                println("Invalid input (${e.message}). Please enter a number.")
            }
        }
    }
}
