package com.AHM.events;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeymapEvent extends Event {

    public final List<Integer> buffer;
    public final EntityPlayerSP player;

    public KeymapEvent(List<Integer> src, EntityPlayerSP plr) {
        this.player = plr;
        this.buffer = new ArrayList<Integer>(src.size());
        this.buffer.addAll(src);
    }

    public static class RecordEvent extends KeymapEvent {
        public RecordEvent(List<Integer> src, EntityPlayerSP plr) { super(src, plr); }
    }

}
