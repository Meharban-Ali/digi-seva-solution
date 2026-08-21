package com.digisevasolution.controller;

import com.digisevasolution.dto.request.CategoryRequest;
import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.CategoryResponse;
import com.digisevasolution.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@Tag(name = "Admin Category Management", description = "Admin endpoints to manage top-level service categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping({"", "/"})
    @Operation(summary = "Get All Categories (Admin)", description = "Fetch all categories with bilingual names, display order, and attached service count.")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategoriesAdmin() {
        List<CategoryResponse> categories = categoryService.getAllCategoriesAdmin();
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully", categories));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Category by ID (Admin)", description = "Fetch single category detail.")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryByIdAdmin(@PathVariable Long id) {
        CategoryResponse category = categoryService.getCategoryByIdAdmin(id);
        return ResponseEntity.ok(ApiResponse.success("Category retrieved successfully", category));
    }

    @PostMapping({"", "/"})
    @Operation(summary = "Create Category (Admin)", description = "Create a new top-level service category.")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse category = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created successfully", category));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Category (Admin)", description = "Update category details.")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse category = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", category));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Category (Admin)", description = "Delete a category. Blocked if services are attached.")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
    }

    @PutMapping("/reorder")
    @Operation(summary = "Reorder Categories (Admin)", description = "Update category display order sequence.")
    public ResponseEntity<ApiResponse<Void>> reorderCategories(@RequestBody List<Long> categoryIds) {
        categoryService.reorderCategories(categoryIds);
        return ResponseEntity.ok(ApiResponse.success("Categories reordered successfully", null));
    }
}
