package ua.kiev.sa;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyCustomClassLoader extends ClassLoader {
    private String path;

    private Map<String, Integer> classHashMap = new HashMap<>();

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    public MyCustomClassLoader(String path) {
        this.path = path;
    }

    @Override
    public Class findClass(String className) {
        File classFile = getFile(path, className);
        byte[] b = loadClassFromFile(classFile);
        classHashMap.put(className, Arrays.hashCode(b));
        return defineClass(className, b, 0, b.length);
    }

    private File getFile(String path, String name) {
        name = (path + name).replaceAll("\\.", "/");
        String filePath = name + ".class";
        return new File(filePath);
    }


    private byte[] loadClassFromFile(File file) {
        byte[] resultData = null;
        try (InputStream is = new FileInputStream(file);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            int tmp = 0;
            while ((tmp = is.read()) != -1) {
                os.write(tmp);
            }
            resultData = os.toByteArray();
        } catch (Exception e) {
            System.out.println("Exception occurred while loading class from file");
            System.out.println("It seems that file has just being rewritten or doesn't exist at all");
            e.printStackTrace();
        }
        return resultData;

    }

    public boolean isClassChanged(String path, String filename) {
        File file= getFile(path,filename);
        return classHashMap.get(filename) != Arrays.hashCode(loadClassFromFile(file));
    }

    private int getClassHash(String path, String filename) {
        if (classHashMap.get(filename) == null) {
            int hashCode = Arrays.hashCode(loadClassFromFile(getFile(path, filename)));
            classHashMap.put(filename, hashCode);
            return hashCode;
        }
        return classHashMap.get(filename);
    }


}


