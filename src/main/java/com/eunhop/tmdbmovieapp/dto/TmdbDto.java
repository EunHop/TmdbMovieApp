package com.eunhop.tmdbmovieapp.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TmdbDto {

  private List<TmdbMovie> tmdbMovies;

  @Getter
  @Builder
  public static class TmdbMovie {
    private String poster_path;
    private String release_date;
    private String title;
  }
}
