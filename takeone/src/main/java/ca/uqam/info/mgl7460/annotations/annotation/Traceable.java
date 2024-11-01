package ca.uqam.info.mgl7460.annotations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Disponible lors de la compilation
@Target(ElementType.METHOD)       // S'applique uniquement aux méthodes
public @interface Traceable {
    String level();

}
