package com.digisevasolution.repository;

import com.digisevasolution.entity.ProjectItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectItemRepository extends JpaRepository<ProjectItem, Long> {

    List<ProjectItem> findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc();

    List<ProjectItem> findByIsActiveTrueAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc();

    Page<ProjectItem> findAllByOrderByDisplayOrderAscCreatedAtDesc(Pageable pageable);
}
