package com.eunhop.tmdbmovieapp.controller;

import com.eunhop.tmdbmovieapp.dto.TmdbDto;
import com.eunhop.tmdbmovieapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;

  @GetMapping("/")
  public String popular(Model model) throws IOException, InterruptedException {
    model.addAttribute("movieList", movieService.popularMovies().getTmdbMovies());
    return "index";
  }
}
