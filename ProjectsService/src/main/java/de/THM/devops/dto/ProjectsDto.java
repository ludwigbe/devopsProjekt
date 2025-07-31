package de.THM.devops.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class ProjectsDto implements Serializable {


    private Long id;

    private String name;

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;


    private String status;

    @NotNull(message = "Customer ID must not be null")
    private Long customersId;

    private String customerName;


}
