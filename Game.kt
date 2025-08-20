class Game {

    val map = GameMap(20, 15)
    val allCombatants = mutableListOf<Combatant>()
    private var isGameOver = false

    init {
        setupGame()
    }

    private fun setupGame() {

        val rifle = Weapon("Rifle", 20, 30,
            Weapon.WeaponType.STANDARD)
        val smg = Weapon("SMG", 15, 40,
            Weapon.WeaponType.STANDARD)

        val player = Player("Player", 100, Team.ALPHA, rifle)
        player.setPosition(1,1)
        allCombatants.add(player)

        val alpha2 = EnemyAI("Alpha-2", 100, Team.ALPHA, smg)
        alpha2.setPosition(1,3)
        allCombatants.add(alpha2)

        val bravo1 = EnemyAI("Bravo-1", 100, Team.BRAVO, rifle)
        bravo1.setPosition(18, 11)
        val bravo2 = EnemyAI("Bravo-2", 100, Team.BRAVO, smg)
        bravo2.setPosition(18, 13)
        allCombatants.add(bravo1)
        allCombatants.add(bravo2)

        for (i in 0..4) {
            map.placeObject(5, 5 + i, GameMap.OBSTACLE)
        }

        for (i in 0..4) {
            map.placeObject(14, 5 + i, GameMap.OBSTACLE)
        }

        map.placeObject(10, 7, GameMap.HEALTH_PACK)
        map.placeObject(3, 10, GameMap.AMMO_PACK)
    }

    fun start() {

        println("--- Team DeathMatch ---")

        while (!isGameOver) {
            map.display(allCombatants)

            for (c in allCombatants.toList()) {
                if (c.isAlive) {
                    c.performAction(this)
                    checkForPowerUps(c)
                    checkWinCondition()
                    if (isGameOver) break
                }
            }
        }
        endGame()
    }

    private fun checkForPowerUps(c : Combatant) {

        when (map.getObjectAt(c.position.x, c.position.y)) {
            GameMap.HEALTH_PACK -> {
                println("${c.name} picked up a health pack!")
                c.heal(30)
                map.placeObject(c.position.x, c.position.y, GameMap.EMPTY)
            }
            GameMap.AMMO_PACK -> {
                println("${c.name} picked up an ammo pack!")
                c.currentWeapon.reload()
                map.placeObject(c.position.x, c.position.y, GameMap.EMPTY)
            }
        }
    }

    private fun checkWinCondition() {

        val alphaTeamAlive = allCombatants.count { it.team == Team.ALPHA && it.isAlive }
        val bravoTeamAlive = allCombatants.count { it.team == Team.BRAVO && it.isAlive }

        if (alphaTeamAlive == 0) {
            println("\n*** Bravo Team Wins! ***")
            isGameOver = true
        }
        else if (bravoTeamAlive == 0) {
            println("\n*** Alpha Team Wins! ***")
            isGameOver = true
        }
    }

    private fun endGame() {

        println("\n--- GAME OVER ---")
    }

    fun isTileOccupiedByCombatant(x: Int, y: Int): Boolean {
        return allCombatants.any { it.isAlive && it.position.x == x && it.position.y == y }
    }
}
