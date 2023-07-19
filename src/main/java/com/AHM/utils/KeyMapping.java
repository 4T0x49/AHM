package com.AHM.utils;

import com.AHM.Config;
import jline.console.KeyMap;
import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Keyboard;

import java.io.*;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Pattern;

public class KeyMapping {
    private static final Trie Mappings = new Trie();
    private static final List<int[]> AllKeys = new ArrayList<int[]>();
    public static final int prefix = Keyboard.KEY_R;
    public static boolean saveOnNew = false;

    public KeyMapping(List<Integer> buffer, String cmd) {
        int[] key = new int[buffer.size()];

        for (int i = 0; i < buffer.size(); ++i) {
            key[i] = buffer.get(i);
        }

        Mappings.insert(key, cmd);
        AllKeys.add(key);
        if (saveOnNew)
            save();
    }

    public static String getCommand(List<Integer> list_key) {
        int[] key = new int[list_key.size()];
        for (int i = 0; i < list_key.size(); ++i) {
            key[i] = list_key.get(i);
        }

        TrieNode node = Mappings.search(key, true);
        if (node == null)
            return null;

        return node.getValue();
    }

    public static int getPossibilities(List<Integer> key) {
        TrieNode node = Mappings.search(key, false);
        if (node == null)
            return 0;

        return node.child_elements;
    }

    public static void save() {
        TrieNode node;

        try {
            File f = new File(Config.KeymapsOutputFile);
            f.createNewFile();
            FileWriter writer = new FileWriter(Config.KeymapsOutputFile);


            for (int i = 0; i < AllKeys.size(); ++i) {
                node = Mappings.search(AllKeys.get(i), false);

                if (i != 0)
                    writer.write("\r\n");
                writer.write(Arrays.toString(AllKeys.get(i)));
                writer.write('|');
                writer.write(node.getValue());
            }

            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void load(){
        File f = new File(Config.KeymapsOutputFile);
        if (!f.exists())
            return;

        try {
            Scanner fileScanner = new Scanner(f);
            Scanner scanner;
            List<Integer> key = new ArrayList<Integer>();
            StringBuilder value;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                System.out.println(line);
                String[] str = line.split("\\|");

                scanner = new Scanner(str[0]);

                scanner.useDelimiter("[^0-9]+");
                while (scanner.hasNextInt())
                    key.add(scanner.nextInt());

                scanner.close();

                value = new StringBuilder(str[1]);
                for (int i = 2; i < str.length; ++i) {
                    value.append(str[i]);
                }

                new KeyMapping(key, value.toString());
                key.clear();
            }
            fileScanner.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
