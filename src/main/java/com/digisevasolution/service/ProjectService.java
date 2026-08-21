package com.digisevasolution.service;

import com.digisevasolution.dto.request.ProjectRequest;
import com.digisevasolution.dto.response.ProjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    List<ProjectResponse> getAllProjectsPublic();

    List<ProjectResponse> getFeaturedProjectsPublic();

    Page<ProjectResponse> getAllProjectsAdmin(Pageable pageable);

    ProjectResponse getProjectById(Long id);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void deleteProject(Long id);
}
