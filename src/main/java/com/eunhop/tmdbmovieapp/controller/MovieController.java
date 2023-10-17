package com.eunhop.tmdbmovieapp.controller;

import com.eunhop.tmdbmovieapp.dto.tmdb.Credits;
import com.eunhop.tmdbmovieapp.dto.tmdb.MovieAndTvDto;
import com.eunhop.tmdbmovieapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Controller
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;
  Random random = new Random();

  @GetMapping("/")
  public String trending(Model model) {
    model.addAttribute("mainList", movieService.trending());
    model.addAttribute("randomNum", random.nextInt(20));
    return "index";
  }
  @GetMapping("/search/{media}")
  public String search(Model model, @RequestParam String query, @PathVariable String media, @RequestParam int page)
      throws ExecutionException, InterruptedException {
    CompletableFuture<MovieAndTvDto> search = CompletableFuture.supplyAsync(() -> {
      return movieService.search(query, media, page);
    });
    CompletableFuture<MovieAndTvDto> trending = CompletableFuture.supplyAsync(movieService::trending);
    CompletableFuture.allOf(search,trending).join();
    model.addAttribute("mainList", search.get());
    model.addAttribute("backdrop", trending.get());
    model.addAttribute("randomNum", random.nextInt(20));
    return "index";
  }
  @GetMapping("/details/{media}/{id}")
  public String Details(Model model, @PathVariable String media, @PathVariable int id)
      throws ExecutionException, InterruptedException {
    CompletableFuture<MovieAndTvDto.Results> detailKR = CompletableFuture.supplyAsync(() -> {
      return movieService.details(media, id, "ko-KR");
    });
    CompletableFuture<MovieAndTvDto.Results> detailEN = CompletableFuture.supplyAsync(() -> {
      return movieService.details(media, id, "en-US");
    });
    CompletableFuture<List<Credits.Person>> credits = CompletableFuture.supplyAsync(() -> {
      if(Objects.equals(media, "movie")) {
        return movieService.credits(id);
      }
      else {
        return movieService.tvCredits(id);
      }
    });
    CompletableFuture.allOf(detailKR,detailEN,credits).join();
    model.addAttribute("descriptionKR", detailKR.get());
    if(Objects.equals(media, "tv")) {
      model.addAttribute("emptyCheck", !detailKR.get().getCreated_by().isEmpty());
    }
    model.addAttribute("descriptionEN", detailEN.get());
    model.addAttribute("producers", credits.get().stream().filter(producer -> producer.getOrder()==null).toList());
    model.addAttribute("actors", credits.get().stream().filter(actor -> actor.getOrder()!=null).toList());
    return "details";
  }

  @GetMapping("/tv_credits")
  @ResponseBody
  public boolean tvCredits() {
    return movieService.details("tv", 120089, "ko-KR").getCreated_by().isEmpty();
  }
}
