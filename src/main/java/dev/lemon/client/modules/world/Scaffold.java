package dev.lemon.client.modules.world;

import dev.lemon.api.module.Module;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.*;

public class Scaffold extends Module {

    public Scaffold() {
        super("Scaffold", Category.WORLD);
    }

    @Getter
    @AllArgsConstructor
    public static class EnumFacingOffset {

        public EnumFacing enumFacing;
        private final Vec3 offset;

    }
}
