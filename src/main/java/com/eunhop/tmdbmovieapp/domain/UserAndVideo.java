package com.eunhop.tmdbmovieapp.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "user_and_video")
@Entity
public class UserAndVideo extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private User user;

  // UserAndMovie.getMovie()는 많지만 Movie.getUserAndMovie()는 없을것 같아 단방향 관계로 지었다. 2023/09/28
  @ManyToOne
  private Video video;

  @Column(length = 100)
  private String review;

  // review만 쓸경우 false, 관심목록 추가 버튼을 눌러야 true로 표시
  private boolean favorite;
}
