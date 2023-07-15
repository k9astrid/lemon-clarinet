package dev.lemon.event;

@FunctionalInterface
public interface IEventListener<Event> {
    void call(Event event);
}