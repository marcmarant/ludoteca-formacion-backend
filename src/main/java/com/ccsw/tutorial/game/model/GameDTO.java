package com.ccsw.tutorial.game.model;

import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.category.model.CategoryDTO;

public class GameDTO {

    private Long id;
    private String title;
    private String age;
    private CategoryDTO category;
    private AuthorDTO author;

    /**
     * @return id
     */
    public Long getId() {

        return this.id;
    }

    /**
     * @param id new value of {@link #getId}.
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * @return title
     */
    public String getTitle() {

        return this.title;
    }

    /**
     * @param title new value of {@link #getTitle}.
     */
    public void setTitle(String title) {

        this.title = title;
    }

    /**
     * @return age
     */
    public String getAge() {

        return this.age;
    }

    /**
     * @param age new value of {@link #getAge}.
     */
    public void setAge(String age) {

        this.age = age;
    }

    /**
     * @return category
     */
    public CategoryDTO getCategory() {

        return this.category;
    }

    /**
     * @param category new value of {@link #getCategory}.
     */
    public void setCategory(CategoryDTO category) {

        this.category = category;
    }

    /**
     * @return author
     */
    public AuthorDTO getAuthor() {

        return this.author;
    }

    /**
     * @param author new value of {@link #getAuthor}.
     */
    public void setAuthor(AuthorDTO author) {

        this.author = author;
    }

}
