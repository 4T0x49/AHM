package com.AHM.commands;


import com.AHM.listeners.Listeners;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;

import java.util.Arrays;
import java.util.List;

public class Rotate implements ICommand {

    public static final String COMMAND_NAME = "rotate";
    private static final String COMMAND_USAGE = "/rotate <yaw> <pitch>";
    private static final List<String> ALIASES = Arrays.asList("rot");
    private static final List<String> TABS = Arrays.asList("0", "45", "135", "180", "-135", "-45");

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
        float yaw = 0f, pitch = 0f;
        switch (args.length) {
            case 0:
                break;
            case 1:
                yaw = Float.parseFloat(args[0]);
                break;
            case 2:
                yaw = Float.parseFloat(args[0]);
                pitch = Float.parseFloat(args[1]);
                break;
        }

        Listeners.rotate.setAngles(yaw, pitch);

        if (!Listeners.rotate.getIsRegistered())
            MinecraftForge.EVENT_BUS.register(Listeners.rotate);
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
