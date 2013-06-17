package com.nitorcreations.wicket.event;

import static com.nitorcreations.wicket.event.CompatibleTypesCache.getCompatibleTypes;
import static java.lang.reflect.Modifier.isPublic;
import static java.util.Arrays.asList;
import static org.apache.wicket.RuntimeConfigurationType.DEVELOPMENT;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Application;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.util.collections.ClassMetaCache;

public class AnnotationEventSink {
    private final ClassMetaCache<Set<Method>> onEventMethodsByParameterType = new ClassMetaCache<Set<Method>>();
    private final ClassMetaCache<Set<Method>> onEventMethodsByPayloadType = new ClassMetaCache<Set<Method>>();

    public AnnotationEventSink(final Class<?> clazz) {
        for (Method method : getAnnotatedMethods(clazz)) {
            if (method.isAnnotationPresent(OnEvent.class)) {
                Class<?> parameterTypes[] = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    continue;
                }
                Class<?> parameterType = parameterTypes[0];
                addOnEventMethodForType(parameterType, method);
            }
        }
    }

    private Collection<Method> getAnnotatedMethods(final Class<?> clazz) {
        Set<Method> annotatedMethods = new HashSet<Method>();
        if (Application.get().getConfigurationType() == DEVELOPMENT) {
            Set<Method> allMethods = new HashSet<Method>();
            allMethods.addAll(asList(clazz.getMethods()));
            allMethods.addAll(asList(clazz.getDeclaredMethods()));
            for (Method method : allMethods) {
                if (method.isAnnotationPresent(OnEvent.class)) {
                    if (!isPublic(method.getModifiers())) {
                        throw new RuntimeException(buildError(method, "@OnEvent annotated methods must be public"));
                    }
                    Class<?> parameterTypes[] = method.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        throw new RuntimeException(buildError(method, "@OnEvent annotated mehods must have exactly one parameter"));
                    }
                    annotatedMethods.add(method);
                }
            }
        } else {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(OnEvent.class) && method.getParameterTypes().length == 1) {
                    annotatedMethods.add(method);
                }
            }
        }
        return annotatedMethods;
    }

    private String buildError(final Method m, final String msg) {
        return new StringBuilder("Invalid @OnEvent annotation in ").append(m).append(": ").append(msg).toString();
    }

    private void addOnEventMethodForType(final Class<?> type, final Method method) {
        Set<Method> methods = onEventMethodsByParameterType.get(type);
        if (methods == null) {
            methods = new HashSet<Method>();
            onEventMethodsByParameterType.put(type, methods);
        }
        methods.add(method);
    }

    public void onEvent(final Object sink, final IEvent<?> event) {
        if (event == null) {
            return;
        }
        Object payload = event.getPayload();
        if (payload == null) {
            return;
        }
        Set<Method> onEventMethods = getOnEventMethods(payload.getClass());
        if (!onEventMethods.isEmpty()) {
            onEvent(onEventMethods, sink, payload);
        }
    }

    private Set<Method> getOnEventMethods(Class<?> payloadType) {
        Set<Method> onEventMethods = onEventMethodsByPayloadType.get(payloadType);
        if (onEventMethods == null) {
            onEventMethods = new HashSet<Method>();
            for (Class<?> type : getCompatibleTypes(payloadType)) {
                Set<Method> methods = onEventMethodsByParameterType.get(type);
                if (methods != null) {
                    onEventMethods.addAll(methods);
                }
            }
            onEventMethodsByPayloadType.put(payloadType, onEventMethods);
        }
        return onEventMethods;
    }

    private void onEvent(final Set<Method> onEventMethods, final Object sink, final Object payload) {
        try {
            for (Method method : onEventMethods) {
                method.invoke(sink, payload);
            }
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Failed to invoke @OnEvent method", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to invoke @OnEvent method", e);
        }
    }
}
