package dev.lemon.api.event;

@FunctionalInterface
public interface IEventListener<Event> {
    void call(Event event);
}