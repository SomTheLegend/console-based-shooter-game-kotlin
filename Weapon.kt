data class Weapon(
    val name: String,
    val damage: Int,
    val ammoCapacity: Int,
    val type: WeaponType
) {

    var currentAmmo: Int = ammoCapacity
        private set

    enum class WeaponType {
        STANDARD, PISTOL, SHOTGUN, MACHINE_GUN
    }

    fun hasAmmo(): Boolean {
        return currentAmmo > 0
    }

    fun useAmmo() {
        if (hasAmmo()) {
            currentAmmo--
        }
    }

    fun reload() {
        currentAmmo = ammoCapacity
        println("$name reloaded. Current ammo: $currentAmmo")

    }
}
