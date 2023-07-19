package com.AHM.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class RotateListener {

    private final Minecraft mc;
    private final Random rand = new Random();
    private boolean isRegistered = false;
    private float yaw;
    private float pitch;
    private float yawPoint;
    private float pitchPoint;
    private int speed = 1;
    private int tick = 0;
    private boolean finish = false;
    private static final float offset = 0.1f;
    private static final float randMax = 1;

    public RotateListener() {
        this.mc = FMLClientHandler.instance().getClient();
    }

    public boolean getIsRegistered() {
        return this.isRegistered;
    }

    public void unregister() {
        MinecraftForge.EVENT_BUS.unregister(this);
        this.isRegistered = false;
        this.tick = 0;
    }

    public void setAngles(float yaw, float pitch) {
        this.finish = false;
        this.yaw = ((((yaw + 180) % 360) + 360) % 360) - 180;
        this.pitch = MathHelper.clamp_float(((((pitch + 180) % 360) + 360) % 360) - 180, -90, 90);
        this.speed = 1;
        this.tick = (int) Math.floor(com.AHM.utils.MathHelper.random(0, 2));
        this.updateAngles();
    }

    public void updateAngles() {
        this.speed += 1;
        float yaw = ((((mc.thePlayer.rotationYaw + 180) % 360) + 360) % 360) - 180;
        float pitch = ((((mc.thePlayer.rotationPitch + 180) % 360) + 360) % 360) - 180;

        float dx = (this.yaw - yaw);
        float dy = (this.pitch - pitch);
        if (dx != 0)
            dx = (float) ((dx < 0) ? -com.AHM.utils.MathHelper.log(Math.abs(dx), 2) : com.AHM.utils.MathHelper.log(Math.abs(dx), 2));
        if (dy != 0)
            dy = (float) ((dy < 0) ? -com.AHM.utils.MathHelper.log(Math.abs(dy), 2) : com.AHM.utils.MathHelper.log(Math.abs(dy), 2));

        this.yawPoint = (float) (((((this.yaw + Math.ceil(dx / speed) + 180) % 360) + 360) % 360) - 180);
        this.pitchPoint = (float) (((((this.pitch + Math.ceil(dy / speed) + 180) % 360) + 360) % 360) - 180);
    }

    private float getRandom() {
        return (float)Math.round(randMax * (2 * this.rand.nextFloat() - 1));
    }

    private void last() {
        this.tick = (int) Math.round(com.AHM.utils.MathHelper.random(20, 50));

        float yaw = mc.thePlayer.rotationYaw;
        float pitch = mc.thePlayer.rotationPitch;
        float dx = yaw - this.yaw;
        float dy = pitch - this.pitch;

        if (dx == 0 && dy == 0) {
            mc.thePlayer.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, this.yaw, this.pitch);
            unregister();
        } else if (dx != 0 && Math.abs(dx) < 0.15) {
            mc.thePlayer.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, this.yaw, pitch);
        } else if (dy != 0 && Math.abs(dy) < 0.15) {
            mc.thePlayer.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, yaw, this.pitch);
        } else {
            mc.thePlayer.setAngles((dx > 0) ? -1 : 1, (dy < 0) ? -1 : 1);
        }

    }

    @SubscribeEvent
    public void tickUpdate(TickEvent.RenderTickEvent event) {
        if (!mc.inGameHasFocus) {
            unregister();
        } else if (event.phase == TickEvent.Phase.START) {
            this.tick -= 1;
            if (this.finish) {
                if (this.tick <= 0)
                    this.last();
                return;
            }

            if (this.tick > 0) {
                return;
            }

            float yaw = ((((mc.thePlayer.rotationYaw + 180) % 360) + 360) % 360) - 180;
            float pitch = ((((mc.thePlayer.rotationPitch + 180) % 360) + 360) % 360) - 180;
            float dx = yaw - this.yawPoint;
            float dy = pitch - this.pitchPoint;

            double calc_dx = Math.sqrt(Math.abs(dx));
            double calc_dy = Math.sqrt(Math.abs(dy));
            float random = getRandom();

            calc_dx = Math.ceil((-offset < dx && dx < offset)
                                    ? 0
                                    : (calc_dx + MathHelper.clamp_double(random, -calc_dx, calc_dx)) / this.speed);
            calc_dy = Math.ceil((-offset < dy && dy < offset)
                                    ? 0
                                    : (calc_dy + MathHelper.clamp_double(random, -calc_dy, calc_dy)) / this.speed);

            if (calc_dx == 0 && calc_dy == 0) {
                if (Math.abs(this.yaw - this.yawPoint) < 0.5 && Math.abs(this.pitch - this.pitchPoint) < 0.5) {
                    this.finish = true;
                } else {
                    this.updateAngles();
                    this.tick = (int) com.AHM.utils.MathHelper.random(40, 60);
                }
            } else {
                mc.thePlayer.setAngles((float) ((dx > 0) ? -calc_dx : calc_dx), (float) ((dy < 0) ? -calc_dy : calc_dy));
            }
        }
    }
}
