package com.eunhop.tmdbmovieapp.repository;

import com.eunhop.tmdbmovieapp.domain.UserAndVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAndVideoRepository extends JpaRepository<UserAndVideo, Long> {
  UserAndVideo findByUserIdAndVideoId(Long userId, int videoId);
  boolean existsByUserIdAndVideoId(Long userId, int videoId);
  UserAndVideo findByVideoId(int videoId);
}
