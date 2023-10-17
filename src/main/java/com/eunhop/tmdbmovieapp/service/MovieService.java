package com.eunhop.tmdbmovieapp.service;

import com.eunhop.tmdbmovieapp.dto.tmdb.Credits;
import com.eunhop.tmdbmovieapp.dto.tmdb.MovieAndTvDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class MovieService {

  @Value("${tmdb.api.key}")
  private String TMDB_API_KEY;

  public MovieAndTvDto trending() {
    return createResults(
        "https://api.themoviedb.org/3/trending/all/day?language=ko-KR",
        MovieAndTvDto.class
    );
  }

  public MovieAndTvDto search(String query, String media, int page) {
    MovieAndTvDto movieAndTvDto = createResults(
        "https://api.themoviedb.org/3/search/" + media + "?query=" + query + "&language=ko-KR&page=" + page,
        MovieAndTvDto.class
    );
    for (int i = 0; i < movieAndTvDto.getResults().size(); i++) {
      movieAndTvDto.getResults().get(i).setMedia_type(media);
    }
    return movieAndTvDto;
  }

  public MovieAndTvDto.Results details(String media, int id, String language) {
    MovieAndTvDto.Results results = createResults(
        "https://api.themoviedb.org/3/" + media + "/" + id + "?language=" + language,
        MovieAndTvDto.Results.class
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
    List<Credits.Person> mainActors = results.getCast().stream().limit(6).toList();
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
    List<Credits.Person> mainActors = results.getCast().stream().limit(6).toList();
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
    return WebClient.create()
        .get()
        .uri(uri)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TMDB_API_KEY)
        .retrieve()
        .bodyToMono(elementClass)
        .block();
  }
}
