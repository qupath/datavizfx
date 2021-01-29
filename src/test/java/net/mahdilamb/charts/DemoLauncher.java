package net.mahdilamb.charts;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("JavaReflectionInvocation")
public class DemoLauncher {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class.forName(args[0]).getMethod("main", String[].class).invoke(null, (Object) null);
    }
}
