package com.AHM.macro;

import net.minecraft.util.Vector3d;

public class MacroBuilder extends Macro {

    public static MacroBuilder currentBuilder;
    private final String name;
    private Vector3d lastEnd;

    public MacroBuilder(String name) {
        this.name = name.toLowerCase();
        currentBuilder = this;
    }

    public void addWalkNode(MacroWalkNode.Direction direction, double start_x, double start_y, double start_z, double end_x, double end_y, double end_z) {
        Vector3d start = new Vector3d();
        start.x = start_x; start.y = start_y; start.z = start_z;
        Vector3d end = new Vector3d();
        end.x = end_x; end.y = end_y; end.z = end_z;
        this.lastEnd = end;

        this.addNode(new MacroWalkNode(direction, start, end));
    }

    public void addWalkNode(MacroWalkNode.Direction direction, double end_x, double end_y, double end_z) {
        Vector3d end = new Vector3d();
        end.x = end_x; end.y = end_y; end.z = end_z;

        this.addNode(new MacroWalkNode(direction, this.lastEnd, end));

        this.lastEnd = end;
    }

    public void addNode(MacroNode node) {
        super.addNode(node);
    }

    public void removeNode(int index) {
        super.removeNode(index);
    }

    public void setRestart(boolean value) {
        super.setRestart(value);
    }

    public static void build() {
        build(true);
    }

    public static void build(boolean save) {
        loadedMacros.put(currentBuilder.name, currentBuilder);
        if (save)
            MacrosLoader.save(currentBuilder.name, currentBuilder);
    }

}
