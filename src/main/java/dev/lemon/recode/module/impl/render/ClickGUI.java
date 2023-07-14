package dev.lemon.recode.module.impl.render;


import dev.lemon.recode.gui.clickgui.ClickGui;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;

import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "ClickGUI", category = Category.RENDER)
public class ClickGUI extends Module {


    @Override
    public void onEnable(){
        mc.displayGuiScreen(new ClickGui());
    }
}