package com.eunhop.tmdbmovieapp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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
