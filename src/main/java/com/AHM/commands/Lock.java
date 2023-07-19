package com.AHM.commands;

import com.AHM.listeners.Listeners;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.Arrays;
import java.util.List;

public class Lock implements ICommand {

    public static final String COMMAND_NAME = "lock";
    private static final String COMMAND_USAGE = "/lock <yaw/pitch>";
    private static final List<String> ALIASES = Arrays.asList("lock");
    private static final List<String> TABS = Arrays.asList("yaw", "pitch");

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

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 1) {
                String arg = args[0];
                EntityPlayerSP player = FMLClientHandler.instance().getClientPlayerEntity();

                boolean isEmpty = Listeners.lock.active.isEmpty();

                if (arg.equalsIgnoreCase("yaw")) {
                    Listeners.lock.active.set(0, !Listeners.lock.active.get(0));
                } else if (arg.equalsIgnoreCase("pitch")) {
                    Listeners.lock.active.set(1, !Listeners.lock.active.get(1));
                } else {
                    sender.addChatMessage(new ChatComponentText("%c" + this.getCommandUsage(sender)));
                    return;
                }


                if (Listeners.lock.active.isEmpty()) {
                    MinecraftForge.EVENT_BUS.unregister(Listeners.lock);
                } else if (isEmpty) {
                    MinecraftForge.EVENT_BUS.register(Listeners.lock);
                }
        } else {
            sender.addChatMessage(new ChatComponentText("&c" + this.getCommandUsage(sender)));
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
