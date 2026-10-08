package net.sodiumzh.nff.girls.gaia.entity;

import gaia.entity.AbstractGaiaEntity;
import net.sodiumzh.nfu.reflection.CachedMethodAccessor;
import net.sodiumzh.nfu.reflection.CachedMethodSearchers;
import net.sodiumzh.nfu.util.NFUReflectionStatics;

import java.lang.reflect.Method;

public class NFFGirlsGaiaEntityUtils {

    public static boolean isMale(AbstractGaiaEntity mob) {
        Class<?> current = mob.getClass();
        Method getter = null;
        while (getter == null
            && current != AbstractGaiaEntity.class
            && AbstractGaiaEntity.class.isAssignableFrom(current.getClass()))
        {
            getter = CachedMethodSearchers.findDeclaredMethod(current, "isMale").orElse(null);
            current = current.getSuperclass();
        }
        if (getter == null) return false;
        return NFUReflectionStatics.invokeMethod(getter, mob).castTo(Boolean.class);
    }

    public static void setMale(AbstractGaiaEntity mob, boolean value) {
        Class<?> current = mob.getClass();
        Method setter = null;
        while (setter == null
            && current != AbstractGaiaEntity.class
            && AbstractGaiaEntity.class.isAssignableFrom(current.getClass()))
        {
            setter = CachedMethodSearchers.findDeclaredMethod(current, "setMale", boolean.class).orElse(null);
            current = current.getSuperclass();
        }
        if (setter != null)
            NFUReflectionStatics.invokeMethod(setter, mob, value);
    }

}
