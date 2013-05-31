package com.nitorcreations.wicket.event;

import static org.apache.commons.lang3.ClassUtils.getAllInterfaces;
import static org.apache.commons.lang3.ClassUtils.getAllSuperclasses;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.util.collections.ClassMetaCache;

class CompatibleTypesCache {
    private static final ClassMetaCache<Set<Class<?>>> allTypesByType = new ClassMetaCache<Set<Class<?>>>();

    static Set<Class<?>> getCompatibleTypes(final Class<?> clazz) {
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
}
