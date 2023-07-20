package dev.lemon.client.modules.render;

import dev.lemon.api.module.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuperheroFX extends Module {
    private final List<String> texts = new ArrayList<>();
    private final ArrayList<Popup> popups = new ArrayList<>();

    public SuperheroFX() {
        super("Superhero FX", Category.RENDER);
        this.texts.addAll(Arrays.asList("POW", "KAPOW", "BOOM", "ZIP", "ZAP", "KABOOM"));
    }

    class Popup {

    }
}
