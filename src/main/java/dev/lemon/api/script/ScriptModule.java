package dev.lemon.api.script;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.client.events.other.ChatEvent;
import dev.lemon.client.events.input.KeyboardInputEvent;
import dev.lemon.client.events.other.TickEvent;
import dev.lemon.client.events.render.Render2DEvent;
import jdk.nashorn.api.scripting.JSObject;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;

public class ScriptModule extends Module {

    private final Logger scriptLogger = LogManager.getLogger();
    private HashMap<String, JSObject> eventMap;

    @Getter
    private final File file;

    @Getter @Setter
    private boolean reloadable = true;

    public ScriptModule(String name, HashMap<String, JSObject> events, String author, File file) {
        super(name, Category.SCRIPTS);
        eventMap = events;
        this.file = file;
        setAuthor(author);
    }

    @Override
    protected void onEnable() {
        if (eventMap.containsKey("enable")) {
            try {
                eventMap.get("enable").call(null);
            } catch (Exception e) {
                scriptLogger.error("Error enable " + this);
                e.printStackTrace();
            }
        }
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        if (eventMap.containsKey("disable")) {
            try {
                eventMap.get("disable").call(null);
            } catch (Exception e) {
                scriptLogger.error("Error disable " + this);
                e.printStackTrace();
            }
        }
        super.onDisable();
    }

    @Subscribe
    private final IEventListener<Render2DEvent> on2DRender = event -> {
        if (eventMap.containsKey("render2D")) {
            try {
                eventMap.get("render2D").call(null);
            } catch (Exception ex) {
                scriptLogger.error("Error 2d " + this);
                ex.printStackTrace();
            }
        }
    };

    @Subscribe
    private final IEventListener<TickEvent> onTick = event -> {
        if (eventMap.containsKey("tick")) {
            try {
                eventMap.get("tick").call(null);
            } catch (Exception ex) {
                scriptLogger.error("Error tick " + this);
                ex.printStackTrace();
            }
        }
    };

    @Subscribe
    private final IEventListener<ChatEvent> onChat = event -> {
        if (eventMap.containsKey("chat")) {
            try {
                eventMap.get("chat").call(null);
            } catch (Exception ex) {
                scriptLogger.error("Error chat " + this);
                ex.printStackTrace();
            }
        }
    };

    @Subscribe
    private final IEventListener<PreMotionEvent> onPreMotion = event -> {
        if (eventMap.containsKey("preMotion")) {
            try {
                eventMap.get("preMotion").call(null);
            } catch (Exception ex) {
                scriptLogger.error("Error preMotion " + this);
                ex.printStackTrace();
            }
        }
    };

    @Subscribe
    private final IEventListener<PreMotionEvent> onPostMotion = event -> {
        if (eventMap.containsKey("postMotion")) {
            try {
                eventMap.get("postMotion").call(null);
            } catch (Exception ex) {
                scriptLogger.error("Error postMotion " + this);
                ex.printStackTrace();
            }
        }
    };

    @Subscribe
    private final IEventListener<KeyboardInputEvent> onKey = event -> {
        if (eventMap.containsKey("key")) {
            try {
                eventMap.get("key").call(null);
            } catch (Exception ex) {
                scriptLogger.error("Error key " + this);
                ex.printStackTrace();
            }
        }
    };
}
