package com.eunhop.tmdbmovieapp.service;

import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.domain.UserAndVideo;
import com.eunhop.tmdbmovieapp.domain.Video;
import com.eunhop.tmdbmovieapp.dto.ReviewDto;
import com.eunhop.tmdbmovieapp.dto.tmdb.Credits;
import com.eunhop.tmdbmovieapp.dto.tmdb.VideoDto;
import com.eunhop.tmdbmovieapp.repository.UserAndVideoRepository;
import com.eunhop.tmdbmovieapp.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class VideoService {

  @Value("${tmdb.api.key}")
  private String TMDB_API_KEY;
  private final UserAndVideoRepository userAndVideoRepository;
  private final VideoRepository videoRepository;

  public VideoDto trending() {
    return createResults(
        "https://api.themoviedb.org/3/trending/all/day?language=ko-KR",
        VideoDto.class
    );
  }

  public VideoDto search(String query, String media, int page) {
    VideoDto videoDto = createResults(
        "https://api.themoviedb.org/3/search/" + media + "?query=" + query + "&language=ko-KR&page=" + page,
        VideoDto.class
    );
    for (int i = 0; i < videoDto.getResults().size(); i++) {
      videoDto.getResults().get(i).setMedia_type(media);
    }
    return videoDto;
  }

  public VideoDto.Results details(String media, int id, String language) {
    VideoDto.Results results = createResults(
        "https://api.themoviedb.org/3/" + media + "/" + id + "?language=" + language,
        VideoDto.Results.class
    );
    assert results != null;
    results.setScore((int) (results.getVote_average() * 10));
    return results;
  }

  public List<Credits.Person> credits(int id) {
    Credits results = createResults(
        "https://api.themoviedb.org/3/movie/" + id + "/credits?language=ko-KR",
        Credits.class
    );
    List<Credits.Person> credits = new ArrayList<>();
    List<Credits.Person> mainActors = results.getCast().stream().limit(10).toList();
    List<Credits.Person> directors = results.getCrew().stream().filter(job -> job.getJob().equals("Director")).toList();
    List<Credits.Person> writers = new ArrayList<>(results.getCrew().stream().filter(job -> job.getJob().equals("Writer")).toList());
    for (Credits.Person director : directors) {
      for (int j = 0; j < writers.size(); j++) {
        if (director.getName().equals(writers.get(j).getName())) {
          director.setJob(director.getJob() + ", " + writers.get(j).getJob());
          writers.remove(writers.get(j));
        }
      }
    }
    credits.addAll(directors);
    credits.addAll(writers);
    credits.addAll(mainActors);
    return credits;
  }

  public List<Credits.Person> tvCredits(int id) {
    Credits results = createResults(
        "https://api.themoviedb.org/3/tv/" + id + "/aggregate_credits?language=ko-KR",
        Credits.class
    );
    List<Credits.Person> mainActors = results.getCast().stream().limit(10).toList();
    for (Credits.Person mainActor : mainActors) {
      for (int j = 0; j < mainActor.getRoles().size(); j++) {
        if (mainActor.getCharacter() == null) {
          mainActor.setCharacter(mainActor.getRoles().get(j).getCharacter());
        } else {
          mainActor.setCharacter(mainActor.getCharacter() + ", " + mainActor.getRoles().get(j).getCharacter());
        }
      }
    }
    return new ArrayList<>(mainActors);
  }

  private <T> T createResults(String uri, Class<T> elementClass) {
    return WebClient.builder()
        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(10*1024*1024))
        .build()
        .get()
        .uri(uri)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TMDB_API_KEY)
        .retrieve()
        .bodyToMono(elementClass)
        .block();
  }

  public List<UserAndVideo> findAllReview(int id) {
    return userAndVideoRepository.findByVideoId(id);
  }

  public List<UserAndVideo> findAllReviewExceptMine(int id, Long userId) {
    List<Long> userList = new ArrayList<>();
    userList.add(userId);
    return userAndVideoRepository.findByVideoIdAndUserIdNotIn(id, userList);
  }

  public UserAndVideo findMyReview(int id, Long userId) {
    return userAndVideoRepository.findByUserIdAndVideoId(userId, id);
  }

  public void saveReview(User user, ReviewDto reviewDto) {
    UserAndVideo existEntity = userAndVideoRepository.findByUserIdAndVideoId(user.getId(), reviewDto.getId());
    if (existEntity != null) {
      existEntity.setReview(reviewDto.getReview());
      userAndVideoRepository.save(existEntity);
    } else {
      Video video = Video.builder()
          .id(reviewDto.getId()).title(reviewDto.getTitle())
          .tagline(reviewDto.getTagline()).poster_path(reviewDto.getPoster_path())
          .media_type(reviewDto.getMedia_type()).release_date(reviewDto.getRelease_date())
          .score(reviewDto.getScore()).build();
      videoRepository.save(video);

      UserAndVideo userAndVideo = UserAndVideo.builder()
          .review(reviewDto.getReview()).wish(false)
          .video(video).user(user).build();
      userAndVideoRepository.save(userAndVideo);
    }
  }

  public void wishSetting(User user, ReviewDto reviewDto) {
    UserAndVideo existEntity = userAndVideoRepository.findByUserIdAndVideoId(user.getId(), reviewDto.getId());
    if (existEntity != null) {
      if (existEntity.isWish()) {
        existEntity.setWish(false);
        userAndVideoRepository.save(existEntity);
      } else {
        existEntity.setWish(true);
        userAndVideoRepository.save(existEntity);
      }
    } else {
      Video video = Video.builder()
          .id(reviewDto.getId()).title(reviewDto.getTitle())
          .tagline(reviewDto.getTagline()).poster_path(reviewDto.getPoster_path())
          .media_type(reviewDto.getMedia_type()).release_date(reviewDto.getRelease_date())
          .score(reviewDto.getScore()).build();
      videoRepository.save(video);

      UserAndVideo userAndVideo = UserAndVideo.builder()
          .wish(true).video(video).user(user).build();
      userAndVideoRepository.save(userAndVideo);
    }
  }

  public List<UserAndVideo> findMyWishlist(User user) {
    return userAndVideoRepository.findByUserIdAndWishOrderByCreatedAt(user.getId(), true);
  }

  public List<UserAndVideo> findMyWishlistOrderByDate(User user) {
    List<UserAndVideo> userAndVideos =  userAndVideoRepository.findByUserIdAndWishOrderByCreatedAt(user.getId(), true);
    return userAndVideos.stream().sorted(Comparator.comparing(userAndVideo -> userAndVideo.getVideo().getRelease_date())).toList();
  }

  public void removeWish(User user, int id) {
    UserAndVideo existEntity = userAndVideoRepository.findByUserIdAndVideoId(user.getId(), id);
    existEntity.setWish(false);
    userAndVideoRepository.save(existEntity);
  }

  public Page<UserAndVideo> findAnyReviews(int pageNo, String sort) {
    Pageable pageable = PageRequest.of(pageNo, 8, Sort.by(Sort.Direction.DESC, sort));
    return userAndVideoRepository.findAll(pageable);
  }

  public void reviewDelete(long id) {
    Optional<UserAndVideo> find = userAndVideoRepository.findById(id);
    userAndVideoRepository.delete(find.get());
  }

  public Page<UserAndVideo> findUserReviews(long id, int pageNo, String sort) {
    Pageable pageable = PageRequest.of(pageNo, 8, Sort.by(Sort.Direction.DESC, sort));
    return userAndVideoRepository.findByUserId(id, pageable);
  }

  public void userReviewsDeleteAll(long userId) {
    List<UserAndVideo> userReviews = userAndVideoRepository.findByUserId(userId);
    userAndVideoRepository.deleteAll(userReviews);
  }
}
