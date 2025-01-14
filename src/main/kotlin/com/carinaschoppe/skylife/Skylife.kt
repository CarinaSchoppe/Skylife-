package com.carinaschoppe.skylife

import com.carinaschoppe.skylife.commands.admin.CreateGamePatternCommand
import com.carinaschoppe.skylife.commands.admin.PlayerAmountCommand
import com.carinaschoppe.skylife.commands.admin.SetLocationCommand
import com.carinaschoppe.skylife.commands.user.*
import com.carinaschoppe.skylife.database.DatabaseConnector
import com.carinaschoppe.skylife.events.player.*
import com.carinaschoppe.skylife.events.skills.SlowFallBootsSkillListener
import com.carinaschoppe.skylife.game.GameLoader
import com.carinaschoppe.skylife.utility.configuration.ConfigurationLoader
import com.carinaschoppe.skylife.utility.configuration.Configurations
import com.carinaschoppe.skylife.utility.messages.Messages
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Skylife : JavaPlugin() {


    //TODO: events
    //TODO: party (maybe)
    //TODO: scoreboard
    //TODO: statistics (kill, death) (done maybe)
    //TODO: Skills
    //TODO: Items
    //TODO: testing (later)
    //TODO: GUIs
    //TODO: Config files with postgreSQL


    companion object {
        lateinit var instance: Skylife
        val folderLocation = "/Skylife/"
    }


    override fun onEnable() {
        // Plugin startup logic
        instance = this
        Configurations.instance = Configurations()
        Messages.instance = Messages()
        initialize(Bukkit.getPluginManager())
        Bukkit.getServer().consoleSender.sendMessage(Messages.instance.PREFIX.append(Component.text("Skylife has been started!", Messages.instance.MESSAGE_COLOR)))
    }

    private fun initialize(pluginManager: PluginManager) {
        ConfigurationLoader.saveConfiguration()
        ConfigurationLoader.loadConfiguration()

        DatabaseConnector.connectDatabase()
        GameLoader.findAllGames().forEach { GameLoader.loadGameFromFile(it) }
        getCommand("join")?.setExecutor(JoinCommand())
        getCommand("start")?.setExecutor(StartCommand())
        getCommand("game")?.setExecutor(CreateGamePatternCommand())
        getCommand("setlocation")?.setExecutor(SetLocationCommand())
        getCommand("playeramount")?.setExecutor(PlayerAmountCommand())
        getCommand("leave")?.setExecutor(LeaveCommand())
        getCommand("stats")?.setExecutor(StatsCommand())
        getCommand("overview")?.setExecutor(GameOverviewCommand())
        getCommand("skills")?.setExecutor(SkillsCommand())

        pluginManager.registerEvents(PlayerJoinsServerListener(), this)
        pluginManager.registerEvents(PlayerLoosesSaturationListener(), this)
        pluginManager.registerEvents(PlayerDisconnectsServerListener(), this)
        pluginManager.registerEvents(PlayerDeathListener(), this)
        pluginManager.registerEvents(PlayerChatsListener(), this)
        pluginManager.registerEvents(PlayerPlacesBlockListener(), this)
        pluginManager.registerEvents(PlayerBreaksBlockListener(), this)
        pluginManager.registerEvents(PlayerMovesIntoGameListener(), this)
        pluginManager.registerEvents(PlayerDamagesListener(), this)
        pluginManager.registerEvents(PlayerSelectGameListener(), this)

        addSkillListeners(pluginManager)

        //Create game_maps folder if
        val folder = File(Bukkit.getServer().worldContainer, "game_maps")

        if (!folder.exists()) {
            folder.mkdir()
        }

    }

    private fun addSkillListeners(pluginManager: PluginManager) {
        pluginManager.registerEvents(SlowFallBootsSkillListener(), this)
    }


    override fun onDisable() {
        // Plugin shutdown logic
        Bukkit.getServer().consoleSender.sendMessage(Messages.instance.PREFIX.append(Component.text("Skylife has been stopped!", Messages.instance.ERROR_COLOR)))
    }
}
