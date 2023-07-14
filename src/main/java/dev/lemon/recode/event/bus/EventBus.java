package dev.lemon.recode.event.bus;

import dev.lemon.recode.event.IEventListener;
import dev.lemon.recode.event.annotations.Subscribe;
import dev.lemon.recode.utils.IMethods;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public final class EventBus<Event> implements Bus<Event>, IMethods {
    private final Map<Type, List<CallSite<Event>>> callSiteMap;
    private final Map<Type, List<IEventListener<Event>>> listenerCache;

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    public EventBus() {
        callSiteMap = new HashMap<>();
        listenerCache = new HashMap<>();
    }

    @Override
    public void register(Object sub) {
        try {
            for (final Field field : sub.getClass().getDeclaredFields()) {
                final Subscribe annotation = field.getAnnotation(Subscribe.class);

                if (annotation != null) {
                    final Type eventType = ((ParameterizedType) (field.getGenericType())).getActualTypeArguments()[0];

                    if (!field.isAccessible())
                        field.setAccessible(true);
                    try {
                        final IEventListener<Event> listener =
                                (IEventListener<Event>) LOOKUP.unreflectGetter(field)
                                        .invokeWithArguments(sub);

                        final byte priority = annotation.value();

                        final List<CallSite<Event>> callSites;
                        final CallSite<Event> callSite = new CallSite<>(sub, listener, priority);

                        if (this.callSiteMap.containsKey(eventType)) {
                            callSites = this.callSiteMap.get(eventType);
                            callSites.add(callSite);
                            callSites.sort((o1, o2) -> o2.priority - o1.priority);
                        } else {
                            callSites = new ArrayList<>(1);
                            callSites.add(callSite);
                            this.callSiteMap.put(eventType, callSites);
                        }
                    } catch (Throwable exception) {
                        exception.printStackTrace();
                    }
                }
            }
            this.populateCache();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unregister(Object sub) {
        for (List<CallSite<Event>> callSites : this.callSiteMap.values()) {
            callSites.removeIf(eventCallSite -> eventCallSite.owner == sub);
        }

        this.populateCache();
    }

    @Override
    public void handle(Event event) {
        final List<IEventListener<Event>> listeners = listenerCache.getOrDefault(event.getClass(), Collections.emptyList());

        int i = 0;
        final int listenersSize = listeners.size();

        while (i < listenersSize) {
            listeners.get(i++).call(event);
        }
    }

    public void handle(final Event event, final Object... listeners) {
        int i = 0;
        final int listenersSize = listeners.length;
        final List<Object> list = Arrays.asList(listeners);
        while (i < listenersSize) {
            ((IEventListener)list.get(i++)).call(event);
        }
    }

    private void populateCache() {
        final Map<Type, List<CallSite<Event>>> callSiteMap = this.callSiteMap;
        final Map<Type, List<IEventListener<Event>>> listenerCache = this.listenerCache;

        for (final Type type : callSiteMap.keySet()) {
            final List<CallSite<Event>> callSites = callSiteMap.get(type);
            final int size = callSites.size();
            final List<IEventListener<Event>> listeners = new ArrayList<>(size);

            for (int i = 0; i < size; i++)
                listeners.add(callSites.get(i).listener);

            listenerCache.put(type, listeners);
        }
    }

    private static class CallSite<Event> {
        private final Object owner;
        private final IEventListener<Event> listener;
        private final byte priority;

        public CallSite(Object owner, IEventListener<Event> listener, byte priority) {
            this.owner = owner;
            this.listener = listener;
            this.priority = priority;
        }
    }
}