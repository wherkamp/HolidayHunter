package me.kingtux.holidayhunter.commands.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface TabCompleter {

    TYPE type();

    String subCommandToEffect() default "";

    public static enum TYPE {
        BASE,
        SUB_COMMAND;
    }
}
