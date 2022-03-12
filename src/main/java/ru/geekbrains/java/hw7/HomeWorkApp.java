package ru.geekbrains.java.hw7;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.util.Random;
import static java.lang.Math.random;

public class HomeWorkApp {
    private static Banana banana = null;

    public static void main(String[] args) {
        Class<?> clazz = Banana.class;
        int[] examination = examinationCountAnnotation(clazz); // проверка на количество AfterSuite и BeforeSuite
        if (examination[0] > 0) {  // запуск BeforeSuite, если есть
            runBeforeSuite(clazz);
        }
        try {
            start(clazz);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (examination[1] > 0) {
            runAfterSuite(clazz);
        }
    }

    private static void runBeforeSuite(Class<?> clazz) {
        System.out.println("      Аннотация BeforeSuite:");
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();
            if (parameters.length == 0) {
                try {
                    constructor.setAccessible(true);
                    banana = (Banana) constructor.newInstance();
                    System.out.println("Отработал конструктор, создан объект класса: " + banana);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void start(Class<?> clazz) throws IllegalAccessException {
        boolean printAnnotation = true;
        for (int i = 1; i < 10; i++) {
            Method[] methods = clazz.getDeclaredMethods();
            printAnnotation = true;
            for (Method method : methods) {
                Test test = method.getAnnotation(Test.class);
                if (test != null) {
                    int priority = method.getAnnotation(Test.class).priority();
                    if (priority == i) {
                        if(printAnnotation) {
                            System.out.println("\n      Аннотация Test(приоритет " + i + "): ");
                            printAnnotation = false;
                        }
                        runTest(clazz, method);
                    }
                }
            }
        }
    }

    private static void runTest(Class<?> clazz, Method method) {
        if(method.getName().startsWith("get")){
            getTest(clazz,method);
        }else {
            if(method.getName().startsWith("set")){
                try {
                    setTest(clazz,method);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                Parameter[] parameters = method.getParameters();
                method.setAccessible(true);
                Object[] castValue = new Object[parameters.length];
                int i = 0;
                for (Parameter parameter : parameters) {
                    if (parameter.getType().equals(int.class)) {
                        castValue[i] = (int) (((int) (random() * 10) + 1) * 17);
                        i++;

                    }
                    if (parameter.getType().equals(boolean.class)) {
                        castValue[i] = true;
                        i++;
                    }
                    if (parameter.getType().equals(long.class)) {
                        Random random = new Random();
                        castValue[i] = (long) random.nextInt() / 100000000;
                        i++;

                    }
                    if (parameter.getType().equals(Object.class)) {
                        castValue[i] = (Object) banana;
                        i++;
                    }
                }
                if (banana != null) {
                    try {
                        System.out.print("Выполнен метод " + method.getName() + " с параметрами: ");
                        for (int j = 0; j < castValue.length; j++) {
                            System.out.print(castValue[j] + " ");
                        }
                        if (method.getReturnType() == int.class) {
                            int returnMethod = (int) method.invoke(banana, castValue);
                            System.out.println("\nМетод вернул: " + returnMethod);
                        }
                        if (method.getReturnType() == boolean.class) {
                            boolean returnMethod = (boolean) method.invoke(banana, castValue);
                            System.out.println("\nМетод вернул: " + returnMethod);
                        }
                        if (method.getReturnType() == String.class) {
                            String returnMethod = (String) method.invoke(banana, castValue);
                            System.out.println("\nМетод вернул: " + returnMethod);
                        }
                        if (method.getReturnType() == LocalDate.class) {
                            LocalDate returnMethod = (LocalDate) method.invoke(banana, castValue);
                            System.out.println("\nМетод вернул: " + returnMethod);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private static void getTest(Class<?> clazz, Method method){
        try {
            method.setAccessible(true);
            if(banana != null) {
                System.out.println("Метод " + method.getName() + " " + " вернул значение: " + method.invoke(banana));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void setTest(Class<?> clazz, Method method) throws InvocationTargetException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String s = method.getName().toLowerCase();
            String f = field.getName().toLowerCase();
            if(s.contains(f)){
                Object castValue = null;
                if(field.getName().equals("name")){
                    castValue = "Банан";
                }
                if(field.getName().equals("weight")){
                    castValue = 200;
                }
                if(field.getName().equals("grade")){
                    castValue = "Кавендиш";
                }
                if(field.getName().equals("expirationDate")){
                    LocalDate date = LocalDate.now();
                    date = date.plusMonths(1);
                    castValue = date;
                }
                if(field.getName().equals("deliveryCountry")){
                    castValue = "Бразилия";
                }
                if(field.getName().equals("permissionToSell")){
                    castValue = true;
                }
                if(banana != null) {
                    method.invoke(banana, castValue);
                    System.out.println("Метод "+method.getName()+" установил значение: "+castValue);
                }
            }
        }
    }

    private static int[] examinationCountAnnotation(Class<?> clazz) {
        int[] count = {0,0};
        for (Method method : clazz.getDeclaredMethods()) {
            AfterSuite a = method.getDeclaredAnnotation(AfterSuite.class);
            BeforeSuite b = method.getDeclaredAnnotation(BeforeSuite.class);
            if (b != null) {
                count[0]++;
            }
            if (a != null) {
                count[1]++;
            }
        }
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            AfterSuite a = constructor.getDeclaredAnnotation(AfterSuite.class);
            BeforeSuite b = constructor.getDeclaredAnnotation(BeforeSuite.class);
            if (b != null) {
                count[0]++;
            }
            if (a != null) {
                count[1]++;
            }
        }
        if (count[1] > 1) {
            throw new RuntimeException("AfterSuite больше 1.");
        }
        if (count[0] > 1) {
            throw new RuntimeException("BeforeSuite больше 1.");
        }
        return count;
    }

    private static void runAfterSuite(Class<?> clazz) {
        System.out.println("\n      Аннотация AfterSuite:");
        for (Method method : clazz.getMethods()) {
            AfterSuite annotation = method.getAnnotation(AfterSuite.class);
            if (annotation != null) {
                method.setAccessible(true);
                try {
                    String s = (String) method.invoke(banana);
                    System.out.println("Выполнен метод " + method.getName());
                    System.out.println("Метод вернул: "+s);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
