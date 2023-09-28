package com.eunhop.tmdbmovieapp.repository;

import com.eunhop.tmdbmovieapp.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
