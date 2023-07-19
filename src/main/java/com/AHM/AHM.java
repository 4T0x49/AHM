package com.AHM;

import com.AHM.commands.*;
import com.AHM.listeners.Listeners;
import com.AHM.macro.MacroBuilder;
import com.AHM.macro.MacroCommandNode;
import com.AHM.macro.MacroWalkNode;
import com.AHM.macro.MacrosLoader;
import com.AHM.utils.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vector3d;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Mod(modid = AHM.MODID, version = AHM.VERSION)
public class AHM
{
    public static final String MODID = "AHM";
    public static final String VERSION = "1.0";
    private Minecraft mc = FMLClientHandler.instance().getClient();
    
    @EventHandler
    public void init(FMLInitializationEvent event) {

        new File(Config.Directory).mkdir();

        MinecraftForge.EVENT_BUS.register(Listeners.keyEvents);

        KeyMapping.load();
        KeyMapping.saveOnNew = true;

        MacrosLoader.load();

        ClientCommandHandler.instance.registerCommand(new Rotate());
        ClientCommandHandler.instance.registerCommand(new Lock());
        ClientCommandHandler.instance.registerCommand(new NewMapping());
        ClientCommandHandler.instance.registerCommand(new Macro());
    }

}
