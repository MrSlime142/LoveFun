package com.github.mrslime142.lovefun.modules

import me.odinmain.features.Category
import me.odinmain.features.Module
import me.odinmain.features.settings.AlwaysActive
import me.odinmain.features.settings.impl.BooleanSetting

@AlwaysActive
object LoveFunModule : Module(
    name = "Love Fun",
    description = "hola",
    category = Category.SKYBLOCK
) {
    val logJson by BooleanSetting(name = "Log JSON", default = false, description = "logs data or somethingS")
    val debugMessages by BooleanSetting(name = "Debug Messages", default = false, description = "debug nonsense")
}