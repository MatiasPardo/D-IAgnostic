package org.diagnostic.domain.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Patient {

    private String document;

    private TypeDocument typeDocument;

    private String hospital;

    private String name;

    private String lastName;

    private String email;

    private String clinicHistory;

    private String detail;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthdate;

}
