package de.THM.devops.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consultants_projects")
public class ConsultantsProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "projects_id", nullable = false)
    private Long projectsId;

    @Column(name = "projects_name", nullable = false)
    private String projectsName;

    @Column(name = "consultants_id", nullable = false)
    private Long consultantsId;

    @Column(name = "consultants_name", nullable = false)
    private String consultantsName;
}
