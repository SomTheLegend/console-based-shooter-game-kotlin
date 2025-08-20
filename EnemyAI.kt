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
            if (currentWeapon.hasAmmo()) {
                println("$name attacks ${target.name}!")
                currentWeapon.useAmmo()
                target.takeDamage(currentWeapon.damage)
                return
            } else {
                println("$name needs to reload!")
                currentWeapon.reload()
                return
            }
        }

        //2.IF NO TARGET, PATROL
        val closestEnemy = findClosestEnemy(allCombatants)
        if (closestEnemy != null) {
            moveTowards(closestEnemy.position, game)
        } else if (patrolPoints.isNotEmpty()) {
            //PATROL LOGIC
            var patrolTarget = patrolPoints[currentPatrolIndex]
            if (position == patrolTarget) {
                currentPatrolIndex = (currentPatrolIndex + 1) % patrolPoints.size
            }
            moveTowards(patrolTarget, game)
        }
    }

    private fun findBestTarget(map: GameMap,
                               allCombatants: List<Combatant>): Combatant? {

        return allCombatants.find {
            it.isAlive && it.team != this.team &&
                    map.hasLineOfSight(this.position, it.position, allCombatants)
        }
    }

    private fun findClosestEnemy(allCombatants: List<Combatant>): Combatant? {

        return allCombatants.filter { it.isAlive && it.team != this.team }
            .minWith(compareBy { this.position.distance(it.position) })
    }
}
