package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.loan.model.Loan;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


import java.io.Serial;
import java.time.LocalDate;

/**
 * Especificación de la entidad {@link Loan} para realizar consultas filtradas.
 *
 * @author Marcos Martínez Antón
 */
public class LoanSpecification implements Specification<Loan> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public LoanSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getValue() == null) return null;
        Path<?> path = getPath(root);
        switch (criteria.getOperation()) {
            case ":":
                if (path.getJavaType() == String.class) {
                    return builder.like(
                            builder.upper(path.as(String.class)),
                            "%" + criteria.getValue().toString().toUpperCase() + "%"
                    );
                }
                return builder.equal(path, criteria.getValue());
            case ">=": // Solo para LocalDate ahora mismo
                if (path.getJavaType() == LocalDate.class) {
                    return builder.greaterThanOrEqualTo(
                            path.as(LocalDate.class),
                            LocalDate.parse(criteria.getValue().toString())
                    );
                }
                return null;
            case "<=": // Solo para LocalDate ahora mismo
                if (path.getJavaType() == LocalDate.class) {
                    return builder.lessThanOrEqualTo(
                            path.as(LocalDate.class),
                            LocalDate.parse(criteria.getValue().toString())
                    );
                }
                return null;
            default:
                return null;
        }
    }

    private Path<?> getPath(Root<Loan> root) {
        String[] split = criteria.getKey().split("[.]", 0);

        Path<?> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }
}

