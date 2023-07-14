package dev.lemon.recode.event;

@FunctionalInterface
public interface IEventListener<Event> {
    void call(Event event);
}