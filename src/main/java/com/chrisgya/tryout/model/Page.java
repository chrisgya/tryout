package com.chrisgya.tryout.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Page<T> {
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalPages;
    private List<T> content;
}
