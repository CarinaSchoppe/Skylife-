package com.carinaschoppe.skylife.game.miscellaneous

import com.carinaschoppe.skylife.game.management.Game
import org.bukkit.Bukkit
import org.bukkit.Location
import java.io.File

object Utility {


    lateinit var mainLocation: Location


    fun locationWorldConverter(location: Location, game: Game): Location {
        return Location(Bukkit.getWorld(game.gameID.toString()), location.x, location.y, location.z, location.yaw, location.pitch)
    }

    fun unloadWorld(game: Game): Boolean {
        val world = Bukkit.getWorld(game.gameID.toString()) ?: return false
        // Stelle sicher, dass alle Spieler aus der Welt teleportiert werden, bevor du sie entlädst
        // Entlade die Welt
        Bukkit.unloadWorld(world, false)

        val folder = File(Bukkit.getServer().worldContainer, world.name)
        if (folder.exists()) {
            return folder.deleteRecursively()
        }
        return false
    }


}