package dev.lemon.api.bot;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.client.events.other.WorldChangeEvent;
import dev.lemon.client.main.Lemon;
import net.minecraft.entity.Entity;

import java.util.ArrayList;

public class BotManager extends ArrayList<Entity> {

    public void initialize() {
        Lemon.INSTANCE.getEventBus().register(this);
    }

    @Subscribe
    private final IEventListener<WorldChangeEvent> onChange = e -> {
        this.clear();
    };

    public boolean add(Entity entity) {
        if (!this.contains(entity))
            super.add(entity);

        return false;
    }

}
