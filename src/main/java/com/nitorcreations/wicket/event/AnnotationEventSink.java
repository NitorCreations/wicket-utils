package com.nitorcreations.wicket.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.util.collections.ClassMetaCache;

import static java.lang.reflect.Modifier.isPublic;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ClassUtils.getAllInterfaces;
import static org.apache.commons.lang3.ClassUtils.getAllSuperclasses;

public class AnnotationEventSink {
    private final ClassMetaCache<Set<Method>> onEventMethodsByType = new ClassMetaCache<Set<Method>>();
    private final ClassMetaCache<Set<Class<?>>> allTypesByType = new ClassMetaCache<Set<Class<?>>>();

    public AnnotationEventSink(final Class<?> clazz) {
        for (Method method : allMethods(clazz)) {
            if (method.isAnnotationPresent(OnEvent.class)) {
                if (!isPublic(method.getModifiers())) {
                    throw new RuntimeException(buildError(method, "@OnEvent annotated methods must be public"));
                }
                Class<?> parameterTypes[] = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException(buildError(method, "@OnEvent annotated mehods must have exactly one parameter"));
                }
                Class<?> parameterType = parameterTypes[0];
                addOnEventMethodForType(parameterType, method);
            }
        }
    }

    private Collection<Method> allMethods(final Class<?> clazz) {
        Set<Method> allMethods = new HashSet<Method>();
        allMethods.addAll(asList(clazz.getMethods()));
        allMethods.addAll(asList(clazz.getDeclaredMethods()));
        return allMethods;
    }

    private String buildError(final Method m, final String msg) {
        return new StringBuilder("Invalid @OnEvent annotation in ").append(m).append(": ").append(msg).toString();
    }

    private void addOnEventMethodForType(final Class<?> type, final Method method) {
        Set<Method> methods = onEventMethodsByType.get(type);
        if (methods == null) {
            methods = new HashSet<Method>();
            onEventMethodsByType.put(type, methods);
        }
        methods.add(method);
    }

    public void onEvent(final Object sink, final IEvent<?> event) {
        if (event != null) {
            Object payload = event.getPayload();
            if (payload != null) {
                for (Class<?> type : getAllTypes(payload.getClass())) {
                    onEventForType(type, sink, payload);
                }
            }
        }
    }

    private Set<Class<?>> getAllTypes(final Class<?> clazz) {
        Set<Class<?>> types = allTypesByType.get(clazz);
        if (types == null) {
            types = new HashSet<Class<?>>();
            types.add(clazz);
            types.addAll(getAllSuperclasses(clazz));
            types.addAll(getAllInterfaces(clazz));
            allTypesByType.put(clazz, types);
        }
        return types;
    }

    private void onEventForType(final Class<?> type, final Object sink, final Object payload) {
        Set<Method> onEventMethodsForType = onEventMethodsByType.get(type);
        if (onEventMethodsForType != null) {
            try {
                for (Method method : onEventMethodsForType) {
                    method.invoke(sink, payload);
                }
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("Failed to invoke onEvent method", e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to invoke onEvent method", e);
            }
        }
    }
}
