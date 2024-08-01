package net.xtb.jumpPad

import com.google.gson.Gson
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

lateinit var config : Configuration
lateinit var dataFile : File
val gson = Gson()

@Suppress("unused")
class JumpPad : JavaPlugin(), TabCompleter {
    override fun onEnable() {
        server.pluginManager.registerEvents(JumpPadInteractHandler(), this)
        val command = getCommand("jumppad")
        command?.setExecutor(JumpPadCommandHandler())
        command?.tabCompleter = this
        this.dataFolder.mkdirs()
        dataFile = File(this.dataFolder, "data.json")
        if(dataFile.exists()) {
            net.xtb.jumpPad.config = gson.fromJson(dataFile.readText(), Configuration::class.java)
        }
        else {
            dataFile.createNewFile()
            net.xtb.jumpPad.config = Configuration()
        }
        logger.info("JumpPad 1.0.0 Enabled!")
    }

    override fun onDisable() {
        dataFile.writeText(gson.toJson(net.xtb.jumpPad.config))
        logger.info("Goodbye!")
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>?): MutableList<String> {
        when(command.name) {
            "jumppad" -> {
                when(args?.getOrNull(0)) {
                    "" -> return arrayListOf("set", "blacklist", "whitelist", "delete", "info")

                    "set" -> {
                        when(args.getOrNull(2)) {
                            null -> return arrayListOf("x", "y")
                            "x" -> return arrayListOf("1")
                            "y" -> return arrayListOf("1")
                        }
                    }
                    "blacklist" -> {
                        when(args.getOrNull(2)) {
                            null -> return arrayListOf("add", "remove", "enable", "disable", "info")
                        }
                    }
                    "whitelist" -> {
                        when(args.getOrNull(2)) {
                            null -> return arrayListOf("add", "remove", "enable", "disable", "info")
                        }
                    }
                }
            }
        }
        return arrayListOf()
    }
}

data class Configuration(
    var isWhiteListEnabled : Boolean = true,
    var isBlackListEnabled : Boolean = false,
    var blackListedLocations : ArrayList<String> = arrayListOf(),
    var whiteListedLocations : ArrayList<String> = arrayListOf(),
    var jumppadVectors : HashMap<String, Pair<Int, Int>> = HashMap()
)
