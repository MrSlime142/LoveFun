package com.github.mrslime142.lovefun.commands.impl

import com.github.mrslime142.lovefun.utils.ChatUtils.modMessage
import com.github.stivais.commodore.Commodore

val LFCommand = Commodore("lf", "lovefun") {
    runs {
        modMessage("hola".trimIndent())
    }
}