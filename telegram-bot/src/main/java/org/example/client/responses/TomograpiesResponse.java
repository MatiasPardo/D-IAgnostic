package org.example.client.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.entities.Tomography;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TomograpiesResponse {

    List<Tomography> tomographies;

    private boolean successful;

    private String error;

}
