package com.AHM.macro;

import com.AHM.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.Buffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacrosLoader {

    public static void save(String name, Macro macro) {

        System.out.println("Saving: " + name);

        try {
            StringBuilder str = new StringBuilder(name);
            str.append(':');
            str.append(macro.restart ? "T" : "F");
            str.append('=');

            Iterator<MacroNode> iter = macro.nodes.iterator();
            MacroNode node;

            while (iter.hasNext()) {
                node = iter.next();
                if (node instanceof MacroWalkNode) {
                    MacroWalkNode walkNode = (MacroWalkNode) node;
                    str.append("walk[");
                    str.append(walkNode.walkingDirection.name());
                    str.append(',');
                    str.append(walkNode.startPos.x);
                    str.append(',');
                    str.append(walkNode.startPos.y);
                    str.append(',');
                    str.append(walkNode.startPos.z);
                    str.append(',');
                    str.append(walkNode.endPos.x);
                    str.append(',');
                    str.append(walkNode.endPos.y);
                    str.append(',');
                    str.append(walkNode.endPos.z);
                } else if (node instanceof MacroCommandNode) {
                    str.append("chat[");
                    str.append(((MacroCommandNode) node).command);
                } else {
                    System.out.println("Error parsing macro file");
                    FMLCommonHandler.instance().exitJava(1, false);
                }
                str.append("]");
                if (iter.hasNext())
                    str.append(',');
            }

            File file = new File(Config.MacrosOutputFile);
            if (file.createNewFile()) {
                System.out.println("Created new file...");
            }

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write(str.toString());
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void load() {
        try {
            File file = new File(Config.MacrosOutputFile);

            if (!file.exists())
                return;

            Scanner lineScanner = new Scanner(file);

            Pattern p1 = Pattern.compile("^([^:]+):(\\w)");
            Pattern p2 = Pattern.compile("(\\w+)\\[([^\\]]+)]");
            Matcher m;

            String[] values;
            String type;

            MacroBuilder builder;

            while (lineScanner.hasNextLine()) {
                String line = lineScanner.nextLine();
                m = p1.matcher(line);

                m.find();
                builder = new MacroBuilder(m.group(1));
                builder.setRestart(m.group(2).charAt(0) == 'T');

                m = p2.matcher(line);

                while (m.find() && m.groupCount() == 2) {
                    type = m.group(1);
                    if (type.equals("walk")) {
                        values = m.group(2).split(",", 7);
                        builder.addWalkNode(MacroWalkNode.Direction.valueOf(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Double.parseDouble(values[4]), Double.parseDouble(values[5]), Double.parseDouble(values[6]));
                    } else if (type.equals("chat")) {
                        builder.addNode(new MacroCommandNode(m.group(2)));
                    }
                }

                MacroBuilder.build(false);
            }

            lineScanner.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
