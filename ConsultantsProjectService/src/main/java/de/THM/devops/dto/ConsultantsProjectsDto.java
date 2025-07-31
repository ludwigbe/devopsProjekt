package de.THM.devops.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantsProjectsDto {

    private Long id; 
    private Long projectsId;
    private String projectsName;
    private Long consultantsId;
    private String consultantsName;
}
