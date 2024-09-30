package di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Map<Class<?>, Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        Map<Class<?>, Object> map = new HashMap<>();
        Queue<Class<?>> queue = new LinkedList<>(classes);

        while (!queue.isEmpty()) {
            Class<?> aClass = queue.poll();
            Constructor<?> constructor = getConstructor(aClass);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (hasBeans(parameterTypes, map)) {
                Object[] parameters = getParameters(parameterTypes, map);
                putBean(constructor, map, aClass, parameters);
                continue;
            }
            queue.add(aClass);
        }
        this.beans = map;
    }

    private Constructor<?> getConstructor(Class<?> aClass) {
        Constructor<?>[] constructors = aClass.getConstructors();
        if (constructors.length != 1) {
            throw new IllegalArgumentException("생성자 주입은 생성자가 1개이어야 합니다.");
        }
        return constructors[0];
    }

    private boolean hasBeans(Class<?>[] parameterTypes, Map<Class<?>, Object> map) {
        if (parameterTypes.length == 0) {
            return true;
        }
        Set<Class<?>> classes = map.keySet();
        for (Class<?> parameterType : parameterTypes) {
            boolean hasBean = false;
            for (Class<?> aClass : classes) {
                if (parameterType.isAssignableFrom(aClass)) {
                    hasBean = true;
                }
            }
            if (!hasBean) {
                return false;
            }
        }
        return true;
    }

    private Object[] getParameters(Class<?>[] parameterTypes, Map<Class<?>, Object> map) {
        return Arrays.stream(parameterTypes)
                .map(parameterType -> getObject(parameterType, map))
                .toArray();
    }

    private void putBean(
            Constructor<?> constructor, Map<Class<?>, Object> map, Class<?> aClass, Object... args
    ) {
        try {
            Object bean = constructor.newInstance(args);
            map.put(aClass, bean);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getObject(Class<?> parameterType, Map<Class<?>, Object> map) {
        return map.entrySet().stream()
                .filter(entry -> parameterType.isAssignableFrom(entry.getKey()))
                .findFirst()
                .map(Entry::getValue)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 빈입니다."));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.get(aClass);
    }
}
