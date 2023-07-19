package com.AHM.macro;

import com.AHM.utils.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vector3d;

public class MacroWalkNode implements MacroNode {

    public enum Direction {
        Left, Forward, Right, Back
    };

    public final Direction walkingDirection;
    public final Vector3d startPos = new Vector3d();
    public final Vector3d endPos = new Vector3d();
    private int tick;
    private boolean ready;

    public MacroWalkNode(Direction walkingDirection, Vector3d startPos, Vector3d endPos) {
        this.walkingDirection = walkingDirection;
        this.startPos.x = Math.floor(startPos.x);
        this.startPos.y = Math.floor(startPos.y);
        this.startPos.z = Math.floor(startPos.z);

        this.endPos.x = Math.floor(endPos.x);
        this.endPos.y = Math.floor(endPos.y);
        this.endPos.z = Math.floor(endPos.z);
    }

    public void setup() {
        this.ready = false;
    }

    // add so it checks if the users has walked past it by storing the previous location and checking if the endPos
    // is between the current block and the last

    @Override
    public boolean nextNode(EntityPlayerSP player) {
        if (this.ready) {
            return (--this.tick) == 0;
        }


        if ((Math.floor(player.posX) == this.endPos.x) && (Math.floor(player.posY) == this.endPos.y) && (Math.floor(player.posZ) == this.endPos.z)) {
            this.ready = true;
            this.tick = (int) MathHelper.random(5, 10);
        }
        return false;
    }

    @Override
    public void run(Minecraft mc, Macro macro) {
        macro.attack = true;
        switch (walkingDirection) {
            case Left:
                macro.left = true;
                break;
            case Forward:
                macro.forward = true;
                break;
            case Right:
                macro.right = true;
                break;
            case Back:
                macro.back = true;
                break;
        }
    }
}
