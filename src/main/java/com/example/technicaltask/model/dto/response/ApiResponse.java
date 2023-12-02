package com.example.technicaltask.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ApiResponse class represents a generic response structure for API endpoints.
 * It encapsulates paginated data along with metadata such as pagination information.
 * - 'content' holds the list of data elements.
 * - 'pageNo' indicates the current page number.
 * - 'pageSize' specifies the number of elements per page.
 * - 'totalElements' represents the total count of elements available.
 * - 'totalPages' indicates the total number of pages.
 * - 'last' is a boolean flag indicating whether the current page is the last page.
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private List<T> content;
    private int pageNo;
    private int pageSize;
    private Long totalElements;
    private int totalPages;
    private boolean last;
}
