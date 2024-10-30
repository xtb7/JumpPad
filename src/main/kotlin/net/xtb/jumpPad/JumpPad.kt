package net.xtb.jumpPad

import com.google.gson.Gson
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor.color
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import net.kyori.adventure.text.logger.slf4j.ComponentLoggerProvider
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.net.HttpURLConnection
import java.net.URI

lateinit var config : Configuration
lateinit var dataFile : File
lateinit var version : String
lateinit var audiences : BukkitAudiences
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
        version = this.description.version
        audiences = BukkitAudiences.create(this)
        try {
            val connection = URI.create("https://api.github.com/repos/xtb7/jumppad/releases").toURL().openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            assert(connection.responseCode == 200) //Throws an error if the request failed
            val reader = gson.newJsonReader(connection.inputStream.bufferedReader()) //Using a reader is more efficient as the whole structure doesn't need to be deserialized
            var actualCount = 0
            val expectedCount = 3 //This should be incremented with every release
            reader.beginArray()
            //Counts how many elements are in the response array (i.e. how many releases there are)
            while (reader.hasNext()) {
                reader.skipValue()
                actualCount++
            }
            reader.close()
            if(actualCount > expectedCount) {
                ComponentLogger.logger().info(text("There is a new version of JumpPad available! Download it at: ", color(0,255,0)).append(text("https://modrinth.com/plugin/jumppad").clickEvent(
                    ClickEvent.openUrl("https://modrinth.com/plugin/jumppad"))))
            }
        }
        catch (_ : Exception) {
            logger.severe("JumpPad cannot access the GitHub API! Is the server connected to the Internet?")
        }
        logger.info("JumpPad v$version Enabled!")
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
                            null -> return arrayListOf("add", "remove", "enable", "info")
                        }
                    }
                    "whitelist" -> {
                        when(args.getOrNull(2)) {
                            null -> return arrayListOf("add", "remove", "enable", "info")
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
    var jumppadVectors : HashMap<String, Pair<Float, Float>> = HashMap()
)
