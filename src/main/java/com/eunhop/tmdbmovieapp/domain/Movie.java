package com.eunhop.tmdbmovieapp.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "movie")
@Entity
public class Movie extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long movieApiId;

  private String title;

  @Column(length = 500)
  private String overview;

  private Float popularity;

  @Column(length = 500)
  private String posterPath;

  private LocalDate release_date;

  private Float voteAverage;

}
