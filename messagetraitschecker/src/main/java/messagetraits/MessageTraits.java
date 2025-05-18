package messagetraits;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Inherited
@Target(ElementType.TYPE) // works only on classes/interfaces
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageTraits {

}
