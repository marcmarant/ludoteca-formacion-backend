package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.Category;
import org.springframework.data.repository.CrudRepository;

/**
 * Repositorio para la entidad {@link Category}
 *
 * @author Marcos Martínez Antón
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
