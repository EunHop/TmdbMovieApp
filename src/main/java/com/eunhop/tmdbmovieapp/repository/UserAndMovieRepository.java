package com.eunhop.tmdbmovieapp.repository;

import com.eunhop.tmdbmovieapp.domain.UserAndMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAndMovieRepository extends JpaRepository<UserAndMovie, Long> {
}
