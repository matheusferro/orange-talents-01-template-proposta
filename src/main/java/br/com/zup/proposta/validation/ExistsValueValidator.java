package br.com.zup.proposta.validation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Validador de existencia de valor cadastrado.
 *
 * @author Matheus Ferro
 * @since 1.0
 */
public class ExistsValueValidator implements ConstraintValidator<ExistsValue, Object> {
    @PersistenceContext
    private EntityManager entityManager;

    private String field;
    private Class<?> klass;
    private boolean nullable;

    @Override
    public void initialize(ExistsValue constraintAnnotation) {
        klass = constraintAnnotation.domainClass();
        field = constraintAnnotation.fieldName();
        nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(nullable && (value == null || value.toString().isEmpty())){
            return true;
        }

        Query query = entityManager.createQuery("SELECT 1 FROM "+klass.getName()+" WHERE "+field+" = :pValor");
        query.setParameter("pValor", value);
        List<?> list = query.getResultList();
        return list.size() >= 1;
    }
}
