package com.hyperhire.whatsapp.dto.response.paged;

import lombok.Data;

import java.util.List;

@Data
public class PagedResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int pageSize;

    // Constructor, getters, and setters
}