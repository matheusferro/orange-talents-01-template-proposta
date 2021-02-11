package br.com.zup.proposta.validation;

import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Validador de valores unicos cadastrado.
 *
 * @param domainClass
 * @param fieldName
 * @author Matheus Ferro
 * @since 1.0
 */
public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {

    @PersistenceContext
    private EntityManager entityManager;

    private String field;
    private Class<?> klass;

    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        klass = constraintAnnotation.domainClass();
        field = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Query query = entityManager.createQuery("SELECT 1 FROM "+klass.getName()+" WHERE "+field+" = :pValor");
        query.setParameter("pValor", value);
        List<?> list = query.getResultList();
        Assert.state(list.size() <= 1, "Foi encontrado mais de um "+klass+" com atributo "+field+" cadastrado.");
        return list.isEmpty();
    }
}
