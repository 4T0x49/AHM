package com.AHM.macro;

import com.AHM.utils.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.ClientCommandHandler;

public class MacroCommandNode implements MacroNode {

    public final String command;
    private Long timer;
    private boolean executed;

    public MacroCommandNode(String command) {
        this.command = command;
    }

    @Override
    public void setup() {
        this.executed = false;
        this.timer = (long) MathHelper.random(50, 400) + System.currentTimeMillis();
    }
    @Override
    public boolean nextNode(EntityPlayerSP player) {
        return this.executed;
    }

    @Override
    public void run(Minecraft mc, Macro macro) {
        if (this.timer > System.currentTimeMillis())
            return;

        this.executed = true;
        if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, this.command) == 0)
            mc.thePlayer.sendChatMessage(this.command);
    }
}
