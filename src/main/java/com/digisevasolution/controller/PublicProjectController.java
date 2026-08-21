package com.digisevasolution.controller;

import com.digisevasolution.dto.response.ApiResponse;
import com.digisevasolution.dto.response.ProjectResponse;
import com.digisevasolution.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class PublicProjectController {

    private final ProjectService projectService;

    public PublicProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjectsPublic() {
        List<ProjectResponse> projects = projectService.getAllProjectsPublic();
        return ResponseEntity.ok(ApiResponse.success("Projects fetched successfully", projects));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getFeaturedProjectsPublic() {
        List<ProjectResponse> projects = projectService.getFeaturedProjectsPublic();
        return ResponseEntity.ok(ApiResponse.success("Featured projects fetched successfully", projects));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable Long id) {
        ProjectResponse project = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success("Project fetched successfully", project));
    }
}
