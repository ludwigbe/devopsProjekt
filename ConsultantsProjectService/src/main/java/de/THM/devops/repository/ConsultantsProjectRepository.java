package de.THM.devops.repository;

import de.THM.devops.model.ConsultantsProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ConsultantsProjectRepository extends JpaRepository<ConsultantsProject, Long> {

    List<ConsultantsProject> findByConsultantsId(Long consultantsId);
    List<ConsultantsProject> findByProjectsId(Long projectsId);

}
