package de.THM.devops.repository;

import de.THM.devops.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectsRepository extends JpaRepository<Project, Long> {

}
