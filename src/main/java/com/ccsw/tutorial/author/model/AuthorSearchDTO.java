package com.ccsw.tutorial.author.model;

import com.ccsw.tutorial.common.pagination.PageableRequest;
import jakarta.validation.constraints.NotNull;

public class AuthorSearchDTO {

    @NotNull(message = "pageable is required")
    private PageableRequest pageable;

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}