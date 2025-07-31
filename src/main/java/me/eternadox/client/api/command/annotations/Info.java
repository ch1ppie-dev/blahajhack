package me.eternadox.client.api.command.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Info {
    String name();
    String[] aliases() default {};
}
