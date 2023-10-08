package com.eunhop.tmdbmovieapp.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TmdbDto {

  private int page;
  private int total_pages;
  private int total_results;
  private List<TmdbMovie> tmdbMovies;

  @Getter
  @Builder
  public static class TmdbMovie {
    private int tmdb_movie_id;
    private String poster_path;
    private String release_date;
    private String first_air_date;
    private String title;
    private String name;
    private String backdrop_path;
    private Integer budget;
    private List<String> genres;
    private String overview;
    private String tagline;
    private Float vote_average;
    private String media_type;
  }
}
