package org.example.client.responses;

import lombok.Getter;
import org.example.entities.Tomography;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TomograpiesResponse {

    List<Tomography> tomographies;

}
