package dev.lemon.recode.event.bus;

public interface Bus<Event> {

    void register(final Object sub);
    void unregister(final Object sub);
    void handle(final Event event);

}