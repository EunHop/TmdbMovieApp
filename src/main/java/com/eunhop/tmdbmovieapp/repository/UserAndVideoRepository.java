package com.eunhop.tmdbmovieapp.repository;

import com.eunhop.tmdbmovieapp.domain.UserAndVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAndVideoRepository extends JpaRepository<UserAndVideo, Long> {
  UserAndVideo findByUserIdAndVideoId(Long userId, int videoId);
  List<UserAndVideo> findByVideoId(int videoId);
  UserAndVideo findByUserId(Long userId);
  List<UserAndVideo> findByVideoIdAndUserIdNotIn(int video_id, List<Long> user_id);
  List<UserAndVideo> findByUserIdAndWishOrderByCreatedAt(Long userId, boolean wish);
}
