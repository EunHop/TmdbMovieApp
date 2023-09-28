package com.eunhop.tmdbmovieapp.repository;

import com.eunhop.tmdbmovieapp.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
