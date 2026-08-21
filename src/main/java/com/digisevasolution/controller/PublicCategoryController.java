package com.digisevasolution.controller;

import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.CategoryResponse;
import com.digisevasolution.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Public Categories", description = "Public endpoints for browsing active service categories")
public class PublicCategoryController {

    private final CategoryService categoryService;

    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping({"", "/"})
    @Operation(summary = "Get Active Categories (Public)", description = "Fetch all active service categories ordered by display order with bilingual fallback.")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getActiveCategoriesPublic(
            @RequestParam(required = false, defaultValue = "en") String lang) {
        List<CategoryResponse> categories = categoryService.getActiveCategoriesPublic(lang);
        return ResponseEntity.ok(ApiResponse.success("Active categories retrieved successfully", categories));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get Category by Slug (Public)", description = "Fetch category detail by URL slug.")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlugPublic(
            @PathVariable String slug,
            @RequestParam(required = false, defaultValue = "en") String lang) {
        CategoryResponse category = categoryService.getCategoryBySlugPublic(slug, lang);
        return ResponseEntity.ok(ApiResponse.success("Category retrieved successfully", category));
    }
}
