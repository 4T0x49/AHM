package com.AHM.macro;

import com.google.gson.InstanceCreator;

import javax.naming.Context;
import java.lang.reflect.Type;

public class MacroInstanceCreator implements InstanceCreator<Macro> {
    private Context context;

    public MacroInstanceCreator(Context context) {
        this.context = context;
    }

    @Override
    public Macro createInstance(Type type) {
        Macro macro = new Macro();
        System.out.println(type.toString());
        return macro;
    }
}
