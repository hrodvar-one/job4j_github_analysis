package ru.job4j.github.analysis.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduledTasks {

    @Autowired
    private final RepositoryService repositoryService;

    @Autowired
    private final GitHubService gitHubService;

    @Scheduled(fixedRateString = "${scheduler.fixedRate}")
    public void fetchCommits() {
        var repositories = repositoryService.getAllRepositories();

        repositories.forEach(repo -> {
            var commits = gitHubService.fetchCommits(repo.getName());
            repositoryService.saveCommits(commits);
        });
    }
}
