package ru.job4j.github.analysis.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.CommitRepository;
import ru.job4j.github.analysis.repository.RepositoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RepositoryService {

    @Autowired
    private final RepositoryRepository repositoryRepository;

    @Autowired
    private final CommitRepository commitRepository;

    public List<Repository> getAllRepositories() {
        return repositoryRepository.findAll();
    }

    @Async("asyncExecutor")
    public void create(Repository repository) {
        repositoryRepository.save(repository);
    }

    public void saveCommits(List<Commit> commits) {
        commitRepository.saveAll(commits);
    }

    public List<Commit> getCommitsByRepository(Long repositoryId) {
        return commitRepository.findByRepositoryId(repositoryId);
    }

    public Repository findByName(String name) {
        return repositoryRepository.findByName(name);
    }
}
