package com.digisevasolution.service.impl;

import com.digisevasolution.dto.request.ProjectRequest;
import com.digisevasolution.dto.response.ProjectResponse;
import com.digisevasolution.entity.ProjectItem;
import com.digisevasolution.exception.ResourceNotFoundException;
import com.digisevasolution.repository.ProjectItemRepository;
import com.digisevasolution.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectItemRepository projectItemRepository;

    public ProjectServiceImpl(ProjectItemRepository projectItemRepository) {
        this.projectItemRepository = projectItemRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjectsPublic() {
        return projectItemRepository.findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc()
                .stream()
                .map(ProjectResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getFeaturedProjectsPublic() {
        return projectItemRepository.findByIsActiveTrueAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc()
                .stream()
                .map(ProjectResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getAllProjectsAdmin(Pageable pageable) {
        return projectItemRepository.findAllByOrderByDisplayOrderAscCreatedAtDesc(pageable)
                .map(ProjectResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) {
        ProjectItem project = projectItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return ProjectResponse.fromEntity(project);
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        ProjectItem project = new ProjectItem();
        mapRequestToEntity(request, project);
        ProjectItem saved = projectItemRepository.save(project);
        return ProjectResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        ProjectItem project = projectItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        mapRequestToEntity(request, project);
        ProjectItem updated = projectItemRepository.save(project);
        return ProjectResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        if (!projectItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project", "id", id);
        }
        projectItemRepository.deleteById(id);
    }

    private void mapRequestToEntity(ProjectRequest request, ProjectItem entity) {
        entity.setTitleEn(request.getTitleEn());
        entity.setTitleHi(request.getTitleHi());
        entity.setDescriptionEn(request.getDescriptionEn());
        entity.setDescriptionHi(request.getDescriptionHi());
        entity.setImageUrl(request.getImageUrl());
        entity.setProjectUrl(request.getProjectUrl());
        entity.setCategoryTag(request.getCategoryTag());
        if (request.getDisplayOrder() != null) {
            entity.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getIsActive() != null) {
            entity.setIsActive(request.getIsActive());
        }
        if (request.getIsFeatured() != null) {
            entity.setIsFeatured(request.getIsFeatured());
        }
    }
}
