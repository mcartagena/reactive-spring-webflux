package com.reactivespring.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class MovieInfo {

    @Id
    private String movieInfoId;
    @NotBlank(message = "MovieInfo.name must be present")
    private String name;
    @Positive(message = "MovieInfo.year must be a positve value")
    @NotNull
    private Integer year;
    private List<@NotBlank(message = "MovieInfo.cast must be present") String> cast;
    private LocalDate release_date;

}
