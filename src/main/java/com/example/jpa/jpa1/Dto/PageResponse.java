package com.example.jpa.jpa1.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
	private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
//
//    public PageResponse(List<T> content, int page, int size, long totalElements, int totalPages) {
//        this.content = content;
//        this.page = page;
//        this.size = size;
//        this.totalElements = totalElements;
//        this.totalPages = totalPages;
//    }

}
