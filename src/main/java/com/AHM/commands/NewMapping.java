package com.AHM.commands;


import com.AHM.events.KeymapEvent;
import com.AHM.listeners.Listeners;
import com.AHM.utils.ChatComponentBuilder;
import com.AHM.utils.KeyMapping;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class NewMapping implements ICommand {

    public static final String COMMAND_NAME = "newmapping";
    private static final String COMMAND_USAGE = "/newmapping <Command>";
    private static final List<String> ALIASES = Arrays.asList("newm");
    private static final List<String> TABS = Arrays.asList();

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender The command sender that executed the command
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return COMMAND_USAGE;
    }

    /**
     * Returns the list of command aliases which behave the same as {@link #getCommandName()}
     *
     * @return The command aliases
     */
    @Override
    public List<String> getCommandAliases() {
        return ALIASES;
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender The command sender that executed the command
     * @param args   The arguments that were passed
     */

    protected NewMappingListener event;
    protected static String command;
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0)
            return;

        StringBuilder str = new StringBuilder(args[0]);

        for (int i = 1; i < args.length; ++i) {
            str.append(' ');
            str.append(args[i]);
        }

        command = str.toString();

        sender.addChatMessage(new ChatComponentText("Recording keymapping sequence ending with ENTER..."));

        if (this.event == null)
            this.event = new NewMappingListener();

        MinecraftForge.EVENT_BUS.register(this.event);
        Listeners.keyEvents.startRecordKeymap();
    }

    public class NewMappingListener extends NewMapping {
        @SubscribeEvent
        public void onRecordFinish(KeymapEvent.RecordEvent event) {
            new KeyMapping(event.buffer, command);
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     *
     * @param sender The command sender that executed the command
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return TABS;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     *
     * @param args  The arguments that were passed
     * @param index Argument index to check
     */
    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
