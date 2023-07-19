package com.AHM.utils;

import java.util.List;

public class Trie {

    private final TrieNode first = new TrieNode();

    public int size() {
        return this.first.child_elements;
    }

    public void insert(int[] key, String value) {
        TrieNode next = first;

        for (int k : key) {
            if (!next.hasChild(k)) {
                next.addChild(k, new TrieNode());
            }

            next.child_elements++;
            next = next.getChild(k);
        }

        next.setValue(value);
    }

    public TrieNode search(int[] key, boolean AllowShortcircuit) {
        TrieNode next = first;

        for (int k : key) {
            next = next.getChild(k);
            if (next == null)
                return null;
        }

        if (AllowShortcircuit) {
            while (next.child_elements == 1) {
                next = next.getFirstChild();
            }
        }

        return next;
    }

    public TrieNode search(List<Integer> key, boolean AllowShortcircuit) {
        TrieNode next = first;

        for (int k : key) {
            next = next.getChild(k);
            if (next == null)
                return null;
        }

        if (AllowShortcircuit) {
            while (next.child_elements == 1) {
                next = next.getFirstChild();
            }
        }

        return next;
    }

}
