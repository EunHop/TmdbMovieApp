package com.eunhop.tmdbmovieapp.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_and_movie")
@Entity
public class UserAndMovie extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private User user;

  // UserAndMovie.getMovie()는 많지만 Movie.getUserAndMovie()는 없을것 같아 단방향 관계로 지었다. 2023/09/28
  @ManyToOne
  private Movie movie;

  @Column(length = 500)
  private String review;

  // review만 쓸경우 false, 관심목록 추가 버튼을 눌러야 true로 표시
  private boolean favorite;
}
