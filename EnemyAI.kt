class EnemyAI(
    name: String,
    health: Int,
    team: Team,
    weapon: Weapon
) : Combatant(name, health, team, weapon) {

    var patrolPoints: List<Point> = emptyList()
    private var currentPatrolIndex = 0;

    override fun performAction(game: Game) {

        if (!isAlive) return

        val map = game.map
        val allCombatants = game.allCombatants

        //1. FIND AND ATTACK THE BEST TARGET
        val target = findBestTarget(map, allCombatants)
        if (target != null) {
            println("$name attacks ${target.name}!")
            target.takeDamage(currentWeapon.damage)
            return
        }

        //2.IF NO TARGET, PATROL
        val closestEnemy = findClosestEnemy(allCombatants)
        if (closestEnemy != null) {
            moveTowards(closestEnemy.position, map)
        } else if (patrolPoints.isNotEmpty()) {
            //PATROL LOGIC
            var patrolTarget = patrolPoints[currentPatrolIndex]
            if (position == patrolTarget) {
                currentPatrolIndex = (currentPatrolIndex + 1) % patrolPoints.size
            }
            moveTowards(patrolTarget, map)
        }
    }

    private fun findBestTarget(map: GameMap,
                               allCombatants: List<Combatant>): Combatant? {

        return allCombatants.find {
            it.isAlive && it.team != this.team &&
                    map.hasLineOfSight(this.position, it.position)
        }
    }

    private fun findClosestEnemy(allCombatants: List<Combatant>): Combatant? {

        return allCombatants.filter { it.isAlive && it.team != this.team }
            .minByOrNull { this.position.distance(it.position) }
    }
}
