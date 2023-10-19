package com.eunhop.tmdbmovieapp.controller;

import com.eunhop.tmdbmovieapp.dto.ReviewDto;
import com.eunhop.tmdbmovieapp.dto.security.PrincipalUser;
import com.eunhop.tmdbmovieapp.dto.tmdb.Credits;
import com.eunhop.tmdbmovieapp.dto.tmdb.VideoDto;
import com.eunhop.tmdbmovieapp.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Controller
@RequiredArgsConstructor
public class VideoController {

  private final VideoService videoService;
  Random random = new Random();

  @GetMapping("/")
  public String trending(Model model) {
    model.addAttribute("mainList", videoService.trending());
    model.addAttribute("randomNum", random.nextInt(20));
    return "index";
  }

  @GetMapping("/search/{media}")
  public String search(Model model, @RequestParam String query, @PathVariable String media, @RequestParam int page)
      throws ExecutionException, InterruptedException {
    CompletableFuture<VideoDto> search = CompletableFuture.supplyAsync(() -> {
      return videoService.search(query, media, page);
    });
    CompletableFuture<VideoDto> trending = CompletableFuture.supplyAsync(videoService::trending);
    CompletableFuture.allOf(search, trending).join();
    model.addAttribute("searchList", search.get());
    model.addAttribute("backdrop", trending.get());
    model.addAttribute("randomNum", random.nextInt(20));
    return "search";
  }

  @GetMapping("/details/{media}/{id}")
  public String Details(Model model, @PathVariable String media, @PathVariable int id,
                        @AuthenticationPrincipal PrincipalUser principalUser)
      throws ExecutionException, InterruptedException {
    CompletableFuture<VideoDto.Results> detailKR = CompletableFuture.supplyAsync(() -> {
      return videoService.details(media, id, "ko-KR");
    });
    CompletableFuture<VideoDto.Results> detailEN = CompletableFuture.supplyAsync(() -> {
      return videoService.details(media, id, "en-US");
    });
    CompletableFuture<List<Credits.Person>> credits = CompletableFuture.supplyAsync(() -> {
      if (Objects.equals(media, "movie")) {
        return videoService.credits(id);
      } else {
        return videoService.tvCredits(id);
      }
    });
    CompletableFuture.allOf(detailKR, detailEN, credits).join();
    model.addAttribute("descriptionKR", detailKR.get());
    if (Objects.equals(media, "tv")) {
      model.addAttribute("emptyCheck", !detailKR.get().getCreated_by().isEmpty());
    }
    model.addAttribute("descriptionEN", detailEN.get());
    model.addAttribute("producers", credits.get().stream().filter(producer -> producer.getOrder() == null).toList());
    model.addAttribute("actors", credits.get().stream().filter(actor -> actor.getOrder() != null).toList());
    if(principalUser!=null) {
      model.addAttribute("user", principalUser);
      model.addAttribute("myReview", videoService.findMyReview(id, principalUser.getUser().getId()));
      model.addAttribute("reviews", videoService.findAllReviewExceptMine(id, principalUser.getUser().getId()));
    } else {
      model.addAttribute("reviews", videoService.findAllReview(id));
    }
    return "details";
  }

  @PostMapping("/details/{media}/{id}/review")
  public String review(@AuthenticationPrincipal PrincipalUser principalUser,
                       ReviewDto reviewDto, @PathVariable String media) {
        videoService.saveReview(principalUser.getUser(), reviewDto);
        return "redirect:/details/{media}/{id}";
  }

  @PutMapping("/details/{media}/{id}/update")
  public String updateReview(@AuthenticationPrincipal PrincipalUser principalUser,
                             @RequestParam String review,
                             @PathVariable int id, @PathVariable String media) {
    videoService.updateReview(principalUser.getUser(), id, review);
    return "redirect:/details/{media}/{id}";
  }
  @PostMapping("/details/{media}/{id}/wish")
  public String addWish(@AuthenticationPrincipal PrincipalUser principalUser,
                        ReviewDto reviewDto,
                        @PathVariable int id, @PathVariable String media) {
    videoService.wishSetting(principalUser.getUser(), reviewDto);
    return "redirect:/details/{media}/{id}";
  }

  @GetMapping("/my_wishlist/{media}")
  public String myWishlist(@AuthenticationPrincipal PrincipalUser principalUser,
                           Model model, @PathVariable String media) {
    model.addAttribute("wishList", videoService.findMyWishlist(principalUser.getUser()));
    return "my_wishlist";
  }
  @PostMapping("/my_wishlist/{media}/wish")
  public String myWishRemove(@AuthenticationPrincipal PrincipalUser principalUser,
                           @RequestParam int id, @PathVariable String media) {
    videoService.removeWish(principalUser.getUser(), id);
    return "redirect:/my_wishlist/{media}";
  }
}

