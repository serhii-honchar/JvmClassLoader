package ua.kiev.sa.utils;

import ua.kiev.sa.MyCustomClassLoader;

import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {

    public Object invokeMethodOfLoadedClassInstance(MyCustomClassLoader customClassLoader, String filename, String methodName)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        Class loadedClass = customClassLoader.findClass(filename);
        Object loadedClassInstance = loadedClass.getDeclaredConstructor().newInstance();
        return loadedClass.getMethod(methodName).invoke(loadedClassInstance);

    }
}
