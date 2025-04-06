package com.github.mrslime142.lovefun

import kotlinx.coroutines.*
import me.odinmain.OdinMain.mc
import com.github.mrslime142.lovefun.modules.LoveFunModule
import me.odinmain.features.ModuleManager
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import kotlin.coroutines.CoroutineContext
import com.github.mrslime142.lovefun.commands.CommandRegistry
import com.github.mrslime142.lovefun.utils.LogHandler

@Mod(modid = "lovefun", useMetadata = true)
class LoveFun {

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        LogHandler

        listOf(
            this
        ).forEach { MinecraftForge.EVENT_BUS.register(it) }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent){
        if (screen == null) return
        LogHandler.Logger.info("Displaying screen $screen")
        mc.displayGuiScreen(screen)
        screen = null
    }

    @Mod.EventHandler
    fun load(event: FMLLoadCompleteEvent) {
        ModuleManager.addModules(
            LoveFunModule
        )
    }

    companion object {
        val version = "@MODVERSION@"

        var screen: GuiScreen? = null
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        fun onOdinLoad() {
            CommandRegistry.register()
        }

        fun launch(context: CoroutineContext = Dispatchers.IO, func: suspend CoroutineScope.() -> Unit) = scope.launch(context) { func(this) }

        fun <R> launchDeferred(fn: suspend () -> R): Deferred<R> {
            val job = CompletableDeferred<R>()
            launch {
                job.complete(fn())
            }
            return job
        }
    }
}