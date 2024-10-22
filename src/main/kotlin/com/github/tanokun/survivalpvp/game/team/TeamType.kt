package com.github.tanokun.survivalpvp.game.team

import org.bukkit.Material

enum class TeamType(val prefix: String, val display: String, val underBlock: Material) {
    BLUE("§9§l[Blue]", "§9§l青", Material.BLUE_WOOL),
    RED("§c§l[RED]", "§c§l赤", Material.RED_WOOL)

}