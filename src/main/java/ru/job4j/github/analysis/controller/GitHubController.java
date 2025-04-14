package ru.job4j.github.analysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.github.analysis.dto.RepositoryCommits;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.service.RepositoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GitHubController {

    @Autowired
    private RepositoryService repositoryService;

    @GetMapping("/repositories")
    public List<Repository> getAllRepositories() {
        return repositoryService.getAllRepositories();
    }

    @GetMapping("/commits/{name}")
    public List<RepositoryCommits> getCommits(@PathVariable(value = "name") String name) {
        var repository = repositoryService.findByName(name);
        if (repository == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Repository not found");
        }
        var commits = repositoryService.getCommitsByRepository(repository.getId());
        return commits.stream()
                .map(commit -> new RepositoryCommits(commit.getMessage(), commit.getAuthor(), commit.getDate()))
                .collect(Collectors.toList());
    }

    @PostMapping("/repository")
    public ResponseEntity<Void> create(@RequestBody Repository repository) {
        repositoryService.create(repository);
        return ResponseEntity.noContent().build();
    }
}
