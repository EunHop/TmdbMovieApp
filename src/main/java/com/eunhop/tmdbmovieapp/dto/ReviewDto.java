package com.eunhop.tmdbmovieapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto{
  // 리뷰를 작성하면 필요한 데이터를 ReviewDto 객체로 담아서 활용함.
  private String review;
  private int id;
  private String title;
  private String tagline;
  private String poster_path;
  private String media_type;
  private String release_date;
  private int score;
}
