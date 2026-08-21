package com.digisevasolution.repository;

import com.digisevasolution.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByOrderByDisplayOrderAscCreatedAtDesc();
    List<Category> findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc();
    Optional<Category> findBySlug(String slug);
    Optional<Category> findBySlugAndIsActiveTrue(String slug);
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, Long id);
}
