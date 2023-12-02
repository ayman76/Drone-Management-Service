package com.example.technicaltask.utils;

import com.example.technicaltask.model.dto.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

public class HelperFunctions {
    /**
     * Utility method to create an ApiResponse object based on the provided pagination details and content.
     * This method constructs an ApiResponse object with pagination metadata and content list.
     */
    public static <T, E> ApiResponse<T> getApiResponse(int pageNo, int pageSize, List<T> content, Page<E> page) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setContent(content);
        apiResponse.setPageNo(pageNo);
        apiResponse.setPageSize(pageSize);
        apiResponse.setTotalElements(page.getTotalElements());
        apiResponse.setTotalPages(page.getTotalPages());
        apiResponse.setLast(page.isLast());
        return apiResponse;
    }

    /**
     * Checks if there are any errors in the BindingResult and returns an appropriate ResponseEntity.
     */
    public static ResponseEntity<StringBuilder> checkBindingResultErrorsAndReturn(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(".\n"));
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return null;
    }
}
