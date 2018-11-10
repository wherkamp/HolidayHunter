package me.kingtux.holidayhunter.commands.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IkeaCommandRules {
     String command();

     String description() default "";

     String format() default "";

     String[] aliases() default "";

}
