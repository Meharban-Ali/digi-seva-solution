package com.digisevasolution.service.impl;

import com.digisevasolution.dto.request.CategoryRequest;
import com.digisevasolution.dto.response.CategoryResponse;
import com.digisevasolution.entity.Category;
import com.digisevasolution.exception.ApiException;
import com.digisevasolution.exception.ResourceNotFoundException;
import com.digisevasolution.repository.CategoryRepository;
import com.digisevasolution.repository.ServiceItemRepository;
import com.digisevasolution.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ServiceItemRepository serviceItemRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ServiceItemRepository serviceItemRepository) {
        this.categoryRepository = categoryRepository;
        this.serviceItemRepository = serviceItemRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategoriesAdmin() {
        return categoryRepository.findAllByOrderByDisplayOrderAscCreatedAtDesc().stream()
                .map(this::mapToAdminResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategoriesPublic(String lang) {
        return categoryRepository.findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc().stream()
                .map(category -> mapToPublicResponse(category, lang))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryBySlugPublic(String slug, String lang) {
        Category category = categoryRepository.findBySlugAndIsActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "slug", slug));
        return mapToPublicResponse(category, lang);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryByIdAdmin(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return mapToAdminResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        String slug = sanitizeOrGenerateSlug(request.getSlug(), request.getNameEn());

        if (categoryRepository.existsBySlug(slug)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Category slug '" + slug + "' already exists.");
        }

        Category category = new Category(
                request.getNameEn().trim(),
                request.getNameHi().trim(),
                slug,
                (request.getIcon() != null && !request.getIcon().isBlank()) ? request.getIcon().trim() : "Folder",
                request.getDisplayOrder() != null ? request.getDisplayOrder() : 0,
                request.getIsActive() != null ? request.getIsActive() : true
        );

        Category saved = categoryRepository.save(category);
        return mapToAdminResponse(saved);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        String slug = sanitizeOrGenerateSlug(request.getSlug(), request.getNameEn());

        if (categoryRepository.existsBySlugAndIdNot(slug, id)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Category slug '" + slug + "' already exists.");
        }

        category.setNameEn(request.getNameEn().trim());
        category.setNameHi(request.getNameHi().trim());
        category.setSlug(slug);
        if (request.getIcon() != null && !request.getIcon().isBlank()) {
            category.setIcon(request.getIcon().trim());
        }
        if (request.getDisplayOrder() != null) {
            category.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getIsActive() != null) {
            category.setActive(request.getIsActive());
        }

        Category updated = categoryRepository.save(category);
        return mapToAdminResponse(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        long serviceCount = serviceItemRepository.countByCategoryId(id);
        if (serviceCount > 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Cannot delete category because " + serviceCount + " service(s) are attached to it. Please reassign or remove services first.");
        }

        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public void reorderCategories(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }
        for (int i = 0; i < categoryIds.size(); i++) {
            Long catId = categoryIds.get(i);
            int orderIndex = i + 1;
            categoryRepository.findById(catId).ifPresent(cat -> {
                cat.setDisplayOrder(orderIndex);
                categoryRepository.save(cat);
            });
        }
    }

    private String sanitizeOrGenerateSlug(String rawSlug, String nameEn) {
        String base = (rawSlug != null && !rawSlug.isBlank()) ? rawSlug : nameEn;
        String slug = base.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        return slug.isBlank() ? "category-" + System.currentTimeMillis() : slug;
    }

    private CategoryResponse mapToAdminResponse(Category category) {
        long serviceCount = serviceItemRepository.countByCategoryId(category.getId());
        return new CategoryResponse(
                category.getId(),
                category.getNameEn(),
                category.getNameHi(),
                null,
                category.getSlug(),
                category.getIcon(),
                category.getDisplayOrder(),
                category.isActive(),
                serviceCount,
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    private CategoryResponse mapToPublicResponse(Category category, String lang) {
        String resolvedName = ("hi".equalsIgnoreCase(lang) && category.getNameHi() != null && !category.getNameHi().isBlank())
                ? category.getNameHi()
                : category.getNameEn();

        long serviceCount = serviceItemRepository.countByCategoryId(category.getId());
        return new CategoryResponse(
                category.getId(),
                category.getNameEn(),
                category.getNameHi(),
                resolvedName,
                category.getSlug(),
                category.getIcon(),
                category.getDisplayOrder(),
                category.isActive(),
                serviceCount,
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
