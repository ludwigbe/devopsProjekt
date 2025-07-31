package de.THM.devops.repository;

import de.THM.devops.model.Entries;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface EntriesRepository extends JpaRepository<Entries, Long> {
    List<Entries> findByConsultantsId(Long consultantsId);

    List<Entries> findByProjectsId(Long projectsId);
}
