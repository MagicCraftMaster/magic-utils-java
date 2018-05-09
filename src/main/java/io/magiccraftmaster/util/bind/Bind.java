package io.magiccraftmaster.util.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@SuppressWarnings("unused")
public @interface Bind {
	String DEFAULT_VALUE = "";
	String value() default DEFAULT_VALUE;
	boolean required() default false;
}
