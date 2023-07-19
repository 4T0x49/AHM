package com.AHM.macro;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Macro {

    private static boolean isActive = false;
    private static Macro macro;
    public static HashMap<String, Macro> loadedMacros = new HashMap<String, Macro>();
    private static Minecraft mc;
    private static EntityPlayerSP player;
    private static Iterator<MacroNode> iter;
    private static MacroNode node;

    public final List<MacroNode> nodes = new ArrayList<MacroNode>();
    public boolean restart = false;
    public boolean attack = false;
    public boolean left = false;
    public boolean forward = false;
    public boolean right = false;
    public boolean back = false;

    public void setup() {
        iter = this.nodes.iterator();
        node = iter.next();
        node.setup();
    }
    public static boolean enable(String name) {
        if (isActive)
            MinecraftForge.EVENT_BUS.unregister(macro);

        macro = loadedMacros.get(name.toLowerCase());
        MinecraftForge.EVENT_BUS.register(macro);
        isActive = true;
        if (macro == null)
            return false;

        mc = FMLClientHandler.instance().getClient();
        player = mc.thePlayer;
        macro.setup();

        return true;
    }

    public static void disable() {
        if (isActive) {
            isActive = false;
            MinecraftForge.EVENT_BUS.unregister(macro);
            disableKeybinds(false);
            macro.updateKeybinds();
        }
    }

    public static void disableKeybinds(boolean attack) {
        macro.attack = attack;
        macro.left = false;
        macro.forward = false;
        macro.right = false;
        macro.back = false;
    }

    private void updateKeybinds() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), this.attack);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), this.left);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), this.forward);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), this.right);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), this.back);
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
        if (!mc.inGameHasFocus)
            return;

        if (node.nextNode(player)) {
            if (iter.hasNext()) {
                node = iter.next();
                node.setup();
                if (node instanceof MacroWalkNode)
                    disableKeybinds(true);
            } else {
                disableKeybinds(false);
                this.updateKeybinds();
                if (this.restart) {
                    this.setup();
                } else {
                    disable();
                }
                return;
            }
        }

        node.run(mc, macro);
        this.updateKeybinds();
    }

    protected void addNode(MacroNode node) {
        this.nodes.add(node);
    }

    protected void removeNode(int index) {
        this.nodes.remove(index);
    }

    protected void setRestart(boolean value) {
        this.restart = value;
    }
}
