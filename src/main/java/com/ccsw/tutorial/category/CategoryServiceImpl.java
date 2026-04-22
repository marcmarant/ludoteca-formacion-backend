package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> findAll() {
        return (List<Category>) this.categoryRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category findById(Long id) throws EntityNotFoundException {

        Optional<Category> category = this.categoryRepository.findById(id);

        if (category.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return category.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(CategoryDTO dto) {

        Category category = new Category();
        category.setName(dto.getName());

        this.categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(CategoryDTO dto) {

        Optional<Category> optCategory = this.categoryRepository.findById(dto.getId());

        if (optCategory.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Category category = optCategory.get();
        category.setName(dto.getName());

        this.categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws EntityNotFoundException {
        if (this.categoryRepository.findById(id).orElse(null) == null) {
            throw new EntityNotFoundException("Categoria " + id.toString() + " no econtrada");
        }
        this.categoryRepository.deleteById(id);
    }

}
