package com.eunhop.tmdbmovieapp.controller;

import com.eunhop.tmdbmovieapp.dto.TmdbDto;
import com.eunhop.tmdbmovieapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;

  @GetMapping("/")
  public String popular(Model model) throws IOException, InterruptedException {
    model.addAttribute("movieList", movieService.popular(null));
    return "index";
  }
  @GetMapping("/search/{media}")
  public String search(Model model, @RequestParam String query, @PathVariable String media, @RequestParam int page) throws IOException, InterruptedException {
    model.addAttribute("movieList", movieService.search(query, media, page));
//    model.addAttribute("media", media);
    return "index";
  }
  @GetMapping("/details/{media_type}/{movie_id}")
  @ResponseBody
  public TmdbDto.TmdbMovie Details(Model model, @PathVariable String media_type, @PathVariable int movie_id) throws IOException, InterruptedException {
    model.addAttribute("movieList", movieService.details(media_type, movie_id));
    return movieService.details(media_type, movie_id);
  }
}
