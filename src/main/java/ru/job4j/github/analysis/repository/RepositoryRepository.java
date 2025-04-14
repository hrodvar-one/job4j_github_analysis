package ru.job4j.github.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryRepository extends JpaRepository<ru.job4j.github.analysis.model.Repository, Long> {
    ru.job4j.github.analysis.model.Repository findByName(String name);
}
