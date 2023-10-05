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

  @Column(length = 100)
  private String title;

  @Column(length = 500)
  private String overview;

  private Float popularity;

  @Column(length = 500)
  private String posterPath;

  @Column(length = 100)
  private String release_date;

  private Float voteAverage;

}
