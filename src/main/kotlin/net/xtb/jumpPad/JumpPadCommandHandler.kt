package net.xtb.jumpPad

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class JumpPadCommandHandler : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when(args.size) {
            0 -> {
                val message = TextComponent("JumpPad v1.0.0 - xtb7/jumppad (Github)")
                message.isUnderlined = true
                message.color = ChatColor.GREEN
                message.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/xtb7/JumpPad")
                return true
            }
            else -> {
                val jumppad = (sender as Player).getTargetBlockExact(5)?:return false
                when(args[0]) {
                    "set" -> {
                        val vector = config.jumppadVectors[Common.locationToString(jumppad.location)]?:Pair(1, 1)
                        when(args.getOrNull(1)) {
                            "x" -> {
                                config.jumppadVectors[Common.locationToString(jumppad.location)] = Pair((args.getOrNull(2)?:return false).toInt(), vector.second)
                                sender.sendMessage("You have successfully set the x multiplier.")
                                return true
                            }
                            "y" -> {
                                config.jumppadVectors[Common.locationToString(jumppad.location)] = Pair(vector.first, (args.getOrNull(2)?:return false).toInt())
                                sender.sendMessage("You have successfully set the y value.")
                                return true
                            }
                        }
                    }
                    "whitelist" -> {
                        when(args.getOrNull(1)) {
                            "add" -> {
                                config.whiteListedLocations.add(Common.locationToString(jumppad.location))
                                sender.sendMessage("You have added this jumppad to the whitelist.")
                            }
                            "remove" -> {
                                config.whiteListedLocations.remove(Common.locationToString(jumppad.location))
                                sender.sendMessage("You have removed this jumppad from the whitelist.")
                            }
                            "enable" -> {
                                config.isWhiteListEnabled = true
                                config.isBlackListEnabled = false
                                sender.sendMessage("You have enabled the whitelist and disabled the blacklist.")
                            }
                            "disable" -> {
                                config.isWhiteListEnabled = false
                                config.isBlackListEnabled = true
                                sender.sendMessage("You have disabled the whitelist and enabled the blacklist.")
                            }
                            "info" -> sender.sendMessage("Selected Jumppad Info:\nIs Whitelisted: ${config.whiteListedLocations.contains(Common.locationToString(jumppad.location))}")
                        }
                        return true
                    }
                    "blacklist" -> {
                        when(args.getOrNull(1)) {
                            "add" -> {
                                config.blackListedLocations.add(Common.locationToString(jumppad.location))
                                sender.sendMessage("You have added this jumppad to the blacklist.")
                            }
                            "remove" -> {
                                config.blackListedLocations.remove(Common.locationToString(jumppad.location))
                                sender.sendMessage("You have removed this jumppad from the blacklist.")
                            }
                            "enable" -> {
                                config.isBlackListEnabled = true
                                config.isWhiteListEnabled = false
                                sender.sendMessage("You have enabled the blacklist and disabled the whitelist.")
                            }
                            "disable" -> {
                                config.isBlackListEnabled = false
                                config.isWhiteListEnabled = true
                                sender.sendMessage("You have disabled the blacklist and enabled the whitelist.")
                            }
                            "info" -> sender.sendMessage("Selected Jumppad Info:\nIs Blacklisted: ${config.blackListedLocations.contains(Common.locationToString(jumppad.location))}")
                        }
                        return true
                    }
                    "delete" -> {
                        val locationString = Common.locationToString(jumppad.location)
                        config.jumppadVectors.remove(locationString)
                        config.blackListedLocations.remove(locationString)
                        config.whiteListedLocations.remove(locationString)
                        sender.sendMessage("You have deleted the data associated with this jumppad.")
                        return true
                    }
                    "info" -> {
                        val vector = config.jumppadVectors[Common.locationToString(jumppad.location)]?:Pair(1,1)
                        sender.sendMessage("X Multiplier: ${vector.first}\nY Value: ${vector.second}")
                        return true
                    }
                }
            }
        }
        return false
    }
}