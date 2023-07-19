package com.AHM.listeners;

import com.AHM.events.KeymapEvent;
import com.AHM.utils.ChatComponentBuilder;
import com.AHM.utils.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class KeyListener {

    private Minecraft mc = FMLClientHandler.instance().getClient();
    private EntityPlayerSP player;
    private Method reset;
    private boolean isListening = false;
    private static final Long TIMEOUT = 800L;
    private Long end;
    private final List<Integer> readBuffer = new ArrayList<Integer>();
    private enum Mode {
        NONE, executeKeymap, recordKeymap
    };
    private Mode activeMode = Mode.NONE;

    public KeyListener() {
        try {
            this.reset = Keyboard.class.getDeclaredMethod("reset");
            this.reset.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void playerJoined(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayerSP) {
            this.player = (EntityPlayerSP) event.entity;
        }
    }

    public void startRecordKeymap() {
        this.activeMode = Mode.recordKeymap;
        this.isListening = true;
    }

    // invoke the private Keyboard.reset method
    private void reset() {
        try {
            this.reset.invoke(Keyboard.class);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void readToBuffer() {
        this.readBuffer.add(Keyboard.getEventKey());
    }

    private void executeKeymap() {
        if (System.currentTimeMillis() < this.end) {
            while (Keyboard.next()) {
                if (!Keyboard.isRepeatEvent() && Keyboard.getEventKeyState()) {
                    this.readToBuffer();

                    if (KeyMapping.getPossibilities(readBuffer) <= 1) {
                        this.end = 0L;
                    } else
                        this.end = System.currentTimeMillis() + TIMEOUT;
                }
            }
        } else {
            this.isListening = false;
            String cmd = KeyMapping.getCommand(readBuffer);

            if (cmd == null) {
                ChatComponentBuilder message =
                        new ChatComponentBuilder("Unable to find a keymap starting with: ", ChatComponentBuilder.Inheritance.NONE)
                                .setColor(EnumChatFormatting.RED);

                for (int i = 0; i < readBuffer.size(); ++i) {
                    if (i != 0)
                        message = message.append(" + ");
                    message = message.append(Keyboard.getKeyName(readBuffer.get(i))).setColor(EnumChatFormatting.GREEN);
                }
                this.player.addChatMessage(message.build());
            } else {
                if (ClientCommandHandler.instance.executeCommand(player, cmd) == 0)
                    player.sendChatMessage(cmd);
            }
            readBuffer.clear();
        }
    }

    private void recordKeymap() {
        while (Keyboard.next()) {
            if (!Keyboard.isRepeatEvent() && Keyboard.getEventKeyState()) {

                if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
                    MinecraftForge.EVENT_BUS.post(new KeymapEvent.RecordEvent(this.readBuffer, this.player));
                    this.isListening = false;
                    this.activeMode = Mode.NONE;
                    this.readBuffer.clear();
                    return;
                } if (Keyboard.getEventKey() == Keyboard.KEY_BACK) {
                    if (this.readBuffer.size() != 0)
                        this.readBuffer.remove(this.readBuffer.size() - 1);
                } else {
                    this.readToBuffer();
                }

                ChatComponentBuilder message =
                        new ChatComponentBuilder("Current mapping: ", ChatComponentBuilder.Inheritance.NONE)
                                .setColor(EnumChatFormatting.RED);

                for (int i = 0; i < readBuffer.size(); ++i) {
                    if (i != 0)
                        message = message.append(" + ");
                    message = message.append(Keyboard.getKeyName(readBuffer.get(i))).setColor(EnumChatFormatting.GREEN);
                }
                this.player.addChatMessage(message.build());
            }
        }
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && this.player != null && mc.inGameHasFocus) {
            if (!this.isListening) {
                if (Keyboard.isRepeatEvent() || !Keyboard.getEventKeyState())
                    return;

                if (Keyboard.getEventKey() == KeyMapping.prefix)
                    this.activeMode = Mode.executeKeymap;
                else
                    return;

                this.end = System.currentTimeMillis() + TIMEOUT;
                this.isListening = true;
            }

            switch (this.activeMode) {
                case executeKeymap:
                    this.executeKeymap();
                    break;
                case recordKeymap:
                    this.recordKeymap();
                    break;
            }


            reset();
        }
    }

}
