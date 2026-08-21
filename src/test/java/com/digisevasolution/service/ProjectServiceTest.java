package com.digisevasolution.service;

import com.digisevasolution.dto.request.ProjectRequest;
import com.digisevasolution.dto.response.ProjectResponse;
import com.digisevasolution.entity.ProjectItem;
import com.digisevasolution.exception.ResourceNotFoundException;
import com.digisevasolution.repository.ProjectItemRepository;
import com.digisevasolution.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectItemRepository projectItemRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private ProjectItem sampleProject;

    @BeforeEach
    void setUp() {
        sampleProject = new ProjectItem();
        sampleProject.setId(1L);
        sampleProject.setTitleEn("E-Commerce Portal");
        sampleProject.setTitleHi("ई-कॉमर्स पोर्टल");
        sampleProject.setDescriptionEn("Full stack online shopping website");
        sampleProject.setCategoryTag("Web Application");
        sampleProject.setImageUrl("https://cloudinary.com/sample.jpg");
        sampleProject.setProjectUrl("https://example.com");
        sampleProject.setDisplayOrder(1);
        sampleProject.setIsActive(true);
        sampleProject.setIsFeatured(true);
    }

    @Test
    void testGetAllProjectsPublic() {
        when(projectItemRepository.findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc())
                .thenReturn(List.of(sampleProject));

        List<ProjectResponse> result = projectService.getAllProjectsPublic();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("E-Commerce Portal", result.get(0).getTitleEn());
    }

    @Test
    void testGetFeaturedProjectsPublic() {
        when(projectItemRepository.findByIsActiveTrueAndIsFeaturedTrueOrderByDisplayOrderAscCreatedAtDesc())
                .thenReturn(List.of(sampleProject));

        List<ProjectResponse> result = projectService.getFeaturedProjectsPublic();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsFeatured());
    }

    @Test
    void testCreateProject() {
        ProjectRequest request = new ProjectRequest();
        request.setTitleEn("E-Commerce Portal");
        request.setCategoryTag("Web Application");
        request.setIsActive(true);

        when(projectItemRepository.save(any(ProjectItem.class))).thenReturn(sampleProject);

        ProjectResponse response = projectService.createProject(request);

        assertNotNull(response);
        assertEquals("E-Commerce Portal", response.getTitleEn());
        verify(projectItemRepository, times(1)).save(any(ProjectItem.class));
    }

    @Test
    void testDeleteProject_Success() {
        when(projectItemRepository.existsById(1L)).thenReturn(true);
        doNothing().when(projectItemRepository).deleteById(1L);

        assertDoesNotThrow(() -> projectService.deleteProject(1L));
        verify(projectItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProject_NotFound() {
        when(projectItemRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> projectService.deleteProject(99L));
    }
}
