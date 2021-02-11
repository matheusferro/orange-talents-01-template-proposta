package br.com.zup.proposta.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Anotacao para utilizar na validacao de existência de valor.
 *
 * @param domainClass
 * @param fieldName
 * @author Matheus Ferro
 * @since 1.0
 */
@Documented
@Constraint(validatedBy = {ExistsValueValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface ExistsValue {
    Class<?> domainClass();

    String fieldName();

    boolean nullable() default false;

    String message() default "{existsValue}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
