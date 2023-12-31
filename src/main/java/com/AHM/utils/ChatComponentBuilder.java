package com.AHM.utils;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.event.HoverEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

// Change it so that the initial element has a reference to the next elements instead of the last linking back

public class ChatComponentBuilder {

    private final IChatComponent parent;

    private String text;
    private ChatStyle style;

    public ChatComponentBuilder(String text) {
        this(text, null, Inheritance.SHALLOW);
    }
    public ChatComponentBuilder(String text, Inheritance inheritance) {
        this(text, null, inheritance);
    }
    private ChatComponentBuilder(String text, IChatComponent parent, Inheritance inheritance) {
        this.parent = parent;
        this.text = text;

        switch (inheritance) {
            case DEEP:
                this.style = parent != null ? parent.getChatStyle() : new ChatStyle();
                break;
            default:
            case SHALLOW:
                this.style = new ChatStyle();
                break;
            case NONE:
                this.style = new ChatStyle().setColor(null).setBold(false).setItalic(false)
                        .setStrikethrough(false).setUnderlined(false).setObfuscated(false)
                        .setChatClickEvent(null).setChatHoverEvent(null).setInsertion(null);
                break;
        }
    }

    public ChatComponentBuilder setColor(EnumChatFormatting color) {
        style.setColor(color);
        return this;
    }

    public ChatComponentBuilder setBold(boolean bold) {
        style.setBold(bold);
        return this;
    }

    public ChatComponentBuilder setItalic(boolean italic) {
        style.setItalic(italic);
        return this;
    }

    public ChatComponentBuilder setStrikethrough(boolean strikethrough) {
        style.setStrikethrough(strikethrough);
        return this;
    }

    public ChatComponentBuilder setUnderlined(boolean underlined) {
        style.setUnderlined(underlined);
        return this;
    }

    public ChatComponentBuilder setObfuscated(boolean obfuscated) {
        style.setObfuscated(obfuscated);
        return this;
    }

    public ChatComponentBuilder setClickEvent(ClickEvent.Action action, String value) {
        style.setChatClickEvent(new ClickEvent(action, value));
        return this;
    }

    public ChatComponentBuilder setHoverEvent(String value) {
        return this.setHoverEvent(new ChatComponentText(value));
    }

    public ChatComponentBuilder setHoverEvent(IChatComponent value) {
        return this.setHoverEvent(Action.SHOW_TEXT, value);
    }

    public ChatComponentBuilder setHoverEvent(HoverEvent.Action action, IChatComponent value) {
        style.setChatHoverEvent(new HoverEvent(action, value));
        return this;
    }

    public ChatComponentBuilder setInsertion(String insertion) {
        style.setInsertion(insertion);
        return this;
    }

    public ChatComponentBuilder append(String text) {
        return this.append(text, Inheritance.SHALLOW);
    }

    public ChatComponentBuilder append(String text, Inheritance inheritance) {
        return new ChatComponentBuilder(text, this.build(), inheritance);
    }

    public IChatComponent build() {
        IChatComponent thisComponent = new ChatComponentText(text).setChatStyle(style);
        return parent != null ? parent.appendSibling(thisComponent) : thisComponent;
    }

    public enum Inheritance {
        DEEP, SHALLOW, NONE
    }

}