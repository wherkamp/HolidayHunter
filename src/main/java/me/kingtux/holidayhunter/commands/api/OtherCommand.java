package me.kingtux.holidayhunter.commands.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface OtherCommand {
    CommandType commandType() default CommandType.FALL_BACK;

    enum CommandType {
        HELP,
        FALL_BACK;
    }
}
