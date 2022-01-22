package annotation;

import common.FETCHTYPE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToOne {
    String columnName() default "";
    String refColumn() default "";
    String tableName() default "";
    FETCHTYPE fetch() default FETCHTYPE.EAGER;
}
