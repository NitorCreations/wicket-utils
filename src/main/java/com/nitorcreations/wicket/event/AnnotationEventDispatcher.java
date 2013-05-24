package com.nitorcreations.wicket.event;

import java.lang.reflect.Method;

import org.apache.wicket.Component;
import org.apache.wicket.IEventDispatcher;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.util.collections.ClassMetaCache;

public class AnnotationEventDispatcher implements IEventDispatcher, IComponentInstantiationListener {
    private final ClassMetaCache<AnnotationEventSink> eventSinkByClass = new ClassMetaCache<AnnotationEventSink>();

    @Override
    public void onInstantiation(final Component component) {
        Class<?> componentClass = component.getClass();
        AnnotationEventSink eventSink = eventSinkByClass.get(componentClass);
        if (eventSink == null && containsOnEventMethod(componentClass)) {
            eventSinkByClass.put(componentClass, new AnnotationEventSink(componentClass));
        }
    }

    private boolean containsOnEventMethod(final Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(OnEvent.class)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dispatchEvent(final Object sink, final IEvent<?> event, final Component component) {
        Class<?> sinkClass = sink.getClass();
        AnnotationEventSink eventSink = eventSinkByClass.get(sinkClass);
        if (eventSink != null) {
            eventSink.onEvent(sink, event);
        }
    }
}
