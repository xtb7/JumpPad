package net.xtb.jumpPad

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class JumpPadInteractHandler : Listener {
    @EventHandler
    fun onPlayerInteractEvent(e : PlayerInteractEvent) {
        when(e.clickedBlock?.type) {
            Material.OAK_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.STONE_PRESSURE_PLATE, Material.MANGROVE_PRESSURE_PLATE, Material.ACACIA_PRESSURE_PLATE, Material.BAMBOO_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE, Material.CHERRY_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.WARPED_PRESSURE_PLATE, Material.CRIMSON_PRESSURE_PLATE, Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE -> {
                if(e.action != Action.PHYSICAL || !e.player.hasPermission("jumppad.use") || (config.isWhiteListEnabled && !config.whiteListedLocations.contains(Common.locationToString(e.clickedBlock!!.location))) || (config.isBlackListEnabled && config.blackListedLocations.contains(Common.locationToString(e.clickedBlock!!.location)))) {
                    return
                }
                val vector = config.jumppadVectors[Common.locationToString(e.clickedBlock!!.location)]?: Pair(1, 1)
                e.player.velocity = e.player.location.direction.multiply(vector.first).setY(vector.second)
            }
            else -> return
        }
    }
}