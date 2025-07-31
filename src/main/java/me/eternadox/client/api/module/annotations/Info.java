package me.eternadox.client.api.module.annotations;

import me.eternadox.client.api.module.Category;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Info {
    String displayName();
    int key() default 0;
    Category category();
    String suffix() default "";
}
