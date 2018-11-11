package me.kingtux.holidayhunter.commands.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.function.Consumer;

@Retention(RetentionPolicy.RUNTIME)

public @interface SubCommand {
    String subcommand();

    String[] alias() default "";
}
