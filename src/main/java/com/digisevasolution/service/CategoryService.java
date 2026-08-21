package com.digisevasolution.service;

import com.digisevasolution.dto.request.CategoryRequest;
import com.digisevasolution.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategoriesAdmin();
    List<CategoryResponse> getActiveCategoriesPublic(String lang);
    CategoryResponse getCategoryBySlugPublic(String slug, String lang);
    CategoryResponse getCategoryByIdAdmin(Long id);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
    void reorderCategories(List<Long> categoryIds);
}
