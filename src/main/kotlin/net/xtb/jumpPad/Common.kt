package net.xtb.jumpPad

import org.bukkit.Location

class Common {
    companion object {
        fun locationToString(location: Location) : String {
            return "${location.world?.name}-${location.blockX}_${location.blockY}-${location.blockZ}"
        }
    }
}