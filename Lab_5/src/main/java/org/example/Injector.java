package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class Injector  {
    private String fileproperties = "prop";

    /**
     * Метод, который принимает в качестве параметра объект любого класса и
     * осуществляет поиск полей, помеченных аннотацией(в качестве типа поля используются некоторый интерфейс),
     * и осуществляет инициализацию полей экземплярами классов,которые указаны в качестве реализации
     * соответствующего интерфейса в некотором файле настроек
     * @param Object Объект любого класса
     * @return Объект после выполнения метода
     * @param <T> Тип объекта
     */
    public <T> T inject(T Object)
    {
        try
        {
            Properties properties = new Properties();
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream in = classloader.getResourceAsStream(fileproperties);
            properties.load(in);
            for (Field field : Object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(AutoInjectable.class)) {
                    field.setAccessible(true);
                    Class<?> cl = Class.forName(properties.getProperty(field.getType().getName()));
                    Object object = cl.getDeclaredConstructor().newInstance();
                    field.set(Object,object);
                }
            }
        }
        catch (IOException|ClassNotFoundException|NoSuchMethodException|
               IllegalAccessException|InstantiationException|InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return Object;
    }
}