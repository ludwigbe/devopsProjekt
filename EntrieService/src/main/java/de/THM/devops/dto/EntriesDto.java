package de.THM.devops.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EntriesDto implements Serializable {

    private Long entryId;

    private Date date;

    private int hours;

    private Long consultantsId;

    private String consultantName;

    private Long projectId;

    private String projectName;
}
