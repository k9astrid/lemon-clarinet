package mc.clpz.base.module.impl.visuals;

import java.awt.*;

import mc.clpz.base.module.Module;

public class FullBright extends Module {
    private float oldGamma;

    public FullBright() {
        super("FullBright", Category.VISUALS, new Color(0xFFDF66).getRGB());
        setRenderLabel("Full Bright");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.oldGamma = getMc().gameSettings.gammaSetting;
        getMc().gameSettings.gammaSetting = 2000f;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getMc().gameSettings.gammaSetting = this.oldGamma;
    }
}
