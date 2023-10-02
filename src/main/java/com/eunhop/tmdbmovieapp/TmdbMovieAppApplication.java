package com.eunhop.tmdbmovieapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class TmdbMovieAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TmdbMovieAppApplication.class, args);
	}

}
