package com.AHM.listeners;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.BitSet;

public class LockListener {

    public final BitSet active = new BitSet(2);

    @SubscribeEvent
    public void tickUpdate(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (this.active.get(0))
                Mouse.getDX(); // reset Mouse.dx before mouseHelper.mouseXYChange() is called from EntityRenderer.updateCameraAndRender
            if (this.active.get(1))
                Mouse.getDY(); // reset Mouse.dy before mouseHelper.mouseXYChange() is called from EntityRenderer.updateCameraAndRender
        }
    }

}
