package annotation;

import common.FETCHTYPE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToMany {
    String tableName() default "";
    String refColumn() default "";
    FETCHTYPE fetch() default FETCHTYPE.LAZY;
}
