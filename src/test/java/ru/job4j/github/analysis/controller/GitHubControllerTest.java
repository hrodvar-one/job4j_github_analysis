package ru.job4j.github.analysis.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.github.analysis.dto.RepositoryCommits;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.service.RepositoryService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GitHubController.class)
class GitHubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepositoryService repositoryService;

    @Test
    void whenGetAllRepositoriesThenReturnOk() throws Exception {
        when(repositoryService.getAllRepositories()).thenReturn(List.of(new Repository()));

        mockMvc.perform(get("/api/repositories"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetCommitsByRepositoryNameThenReturnOk() throws Exception {
        var repositoryName = "test-repo";

        var commits = List.of(
                new Commit(1L, "Initial commit", "Author1", LocalDateTime.now(), null),
                new Commit(2L, "Added README", "Author2", LocalDateTime.now(), null)
        );

        var commitsDto = commits.stream()
                .map(commit -> new RepositoryCommits(commit.getMessage(), commit.getAuthor(), commit.getDate()))
                .toList();

        when(repositoryService.findByName(repositoryName))
                .thenReturn(new Repository(1L, repositoryName, "http://example.com"));
        when(repositoryService.getCommitsByRepository(anyLong()))
                .thenReturn(commits);

        mockMvc.perform(get("/api/commits/{name}", repositoryName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value(commitsDto.get(0).getMessage()))
                .andExpect(jsonPath("$[0].author").value(commitsDto.get(0).getAuthor()))
                .andExpect(jsonPath("$[1].message").value(commitsDto.get(1).getMessage()))
                .andExpect(jsonPath("$[1].author").value(commitsDto.get(1).getAuthor()));
    }

    @Test
    void whenCreateRepositoryThenReturnNoContent() throws Exception {
        var repository = new Repository();
        repository.setName("test-repo");
        repository.setUrl("http://github.com/test-repo");

        doNothing().when(repositoryService).create(any(Repository.class));

        mockMvc.perform(post("/api/repository")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                             {
                                 "name": "test-repo",
                                 "url": "http://github.com/test-repo"
                             }
                             """))
                .andExpect(status().isNoContent());
    }
}