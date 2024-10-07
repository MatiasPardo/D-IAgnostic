package org.diagnostic.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Patient {

    private String document;

    private TypeDocument typeDocumentti;

    private String hospital;

    private String name;

    private String lastName;

    private String email;

    private String clinicHistory;

    private String detail;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/mm/yyyy HH:mm:ss")
    private LocalDateTime birthdate;

}
