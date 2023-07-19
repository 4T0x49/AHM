package com.AHM.macro;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public interface MacroNode {

    void setup();
    boolean nextNode(EntityPlayerSP player);
    void run(Minecraft mc, Macro macro);

}
