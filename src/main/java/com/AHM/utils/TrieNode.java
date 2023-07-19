package com.AHM.utils;

import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TrieNode {
    private String value;
    public int child_elements = 0;
    private TrieNode[] next;

    public boolean hasChild(int key) {
        return this.next != null && this.next[key] != null;
    }

    public TrieNode getFirstChild() {
        if (this.child_elements == 0)
            return null;

        for (TrieNode node : this.next) {
            if (node != null)
                return node;
        }

        return null; // Unnecessary
    }

    public void addChild(int key, TrieNode value) {
        if (this.next == null)
            this.next = new TrieNode[Keyboard.KEYBOARD_SIZE];

        assert(!this.hasChild(key)); // make sure it is not set previously
        this.next[key] = value;
    }

    public TrieNode getChild(int key) {
        if (this.next == null)
            return null;

        return this.next[key];
    }

    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
