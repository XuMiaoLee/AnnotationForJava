package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by admin on 2018/3/30.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface PrimaryKey
{
}
