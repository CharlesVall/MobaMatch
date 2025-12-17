package io.github.charlesvall.mobamatch.infrastructure.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageDto<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <T> PageDto<T> of(Page<T> page) {
        return new PageDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}