package com.AHM.commands;

import com.AHM.listeners.Listeners;
import com.AHM.macro.*;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import tv.twitch.chat.Chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Macro implements ICommand {

    public static final String COMMAND_NAME = "macro";
    private static final String COMMAND_USAGE = "/macro <enable/disable/setup> <name>";
    private static final List<String> ALIASES = Arrays.asList("lock");
    private static final List<String> TABS = Arrays.asList("enable", "setup");
    private MacroWalkNode.Direction direction;
    private Vector3d start;

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
        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText(this.getCommandUsage(sender)));
            return;
        }

        if (args[0].equalsIgnoreCase("setup")) {
            switch (args.length) {
                case 2:
                    if (args[1].equalsIgnoreCase("start")) {
                        EntityPlayerSP player = FMLClientHandler.instance().getClientPlayerEntity();
                        this.start = new Vector3d();
                        this.start.x = player.posX;
                        this.start.y = player.posY;
                        this.start.z = player.posZ;
                    } else if (args[1].equalsIgnoreCase("end")) {
                        EntityPlayerSP player = FMLClientHandler.instance().getClientPlayerEntity();
                        Vector3d end = new Vector3d();
                        end.x = player.posX;
                        end.y = player.posY;
                        end.z = player.posZ;
                        MacroNode node = new MacroWalkNode(this.direction, this.start, end);
                        MacroBuilder.currentBuilder.addNode(node);
                        this.start = null;
                    } else if (args[1].equalsIgnoreCase("done")) {
                        MacroBuilder.currentBuilder.setRestart(true);
                        MacroBuilder.build();
                    } else {
                        new MacroBuilder(args[1]);
                        sender.addChatMessage(new ChatComponentText("You are now creating a new farming macro:"));
                        sender.addChatMessage(new ChatComponentText("To add a new walk node: /macro setup walk <LEFT/FORWARD/RIGHT/BACK>"));
                        sender.addChatMessage(new ChatComponentText("To set walk start pos: /macro setup start"));
                        sender.addChatMessage(new ChatComponentText("To set walk end pos: /macro setup end"));
                        sender.addChatMessage(new ChatComponentText("To add a new chat node: /macro setup chat <message>"));
                        sender.addChatMessage(new ChatComponentText("When you are done: /macro setup done"));
                        break;
                    }
                    break;
                case 3:
                    if (args[1].equalsIgnoreCase("walk")) {
                        if (args[2].equalsIgnoreCase("left")) {
                            this.direction = MacroWalkNode.Direction.Left;
                        } else if (args[2].equalsIgnoreCase("forward")) {
                            this.direction = MacroWalkNode.Direction.Forward;
                        } else if (args[2].equalsIgnoreCase("right")) {
                            this.direction = MacroWalkNode.Direction.Right;
                        } else if (args[2].equalsIgnoreCase("back")) {
                            this.direction = MacroWalkNode.Direction.Back;
                        }
                        return;
                    }
                default:
                    if (args[1].equalsIgnoreCase("chat")) {
                        StringBuilder str = new StringBuilder(args[2]);
                        for (int i = 3; i < args.length; ++i) {
                            str.append(' ');
                            str.append(args[i]);
                        }
                        MacroBuilder.currentBuilder.addNode(new MacroCommandNode(str.toString()));
                    } else {
                        sender.addChatMessage(new ChatComponentText(this.getCommandUsage(sender)));
                    }



            }
        } else if (args[0].equalsIgnoreCase("enable")) {
            if (com.AHM.macro.Macro.enable(args[1].toLowerCase()))
                sender.addChatMessage(new ChatComponentText("Started macro: " + args[1]));
            else
                sender.addChatMessage(new ChatComponentText("Unable to find macro: " + args[1]));
        } else if (args[0].equalsIgnoreCase("disable")) {
            com.AHM.macro.Macro.disable();
        } else if (args[0].equalsIgnoreCase("reload")) {
            com.AHM.macro.Macro.disable();
            com.AHM.macro.Macro.loadedMacros.clear();
            MacrosLoader.load();
        } else {
            sender.addChatMessage(new ChatComponentText(this.getCommandUsage(sender)));
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
        if (args.length == 1)
            return TABS;

        return new ArrayList<String>(com.AHM.macro.Macro.loadedMacros.keySet());
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
