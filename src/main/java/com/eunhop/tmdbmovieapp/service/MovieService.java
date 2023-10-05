package com.eunhop.tmdbmovieapp.service;

import com.eunhop.tmdbmovieapp.dto.TmdbDto;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieService {

  @Value("${tmdb.api.key}")
  private String TMDB_API_KEY;

  public TmdbDto popularMovies() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.themoviedb.org/3/movie/popular?language=ko-KR&page=1"))
        .header("accept", "application/json")
        .header("Authorization", "Bearer " + TMDB_API_KEY)
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    JsonArray list = JsonParser.parseString(response.body()).getAsJsonObject().getAsJsonArray("results");
    List<TmdbDto.TmdbMovie> tmdbMovies = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      String poster_path = list.get(i).getAsJsonObject().get("poster_path").getAsString();
      String release_date = list.get(i).getAsJsonObject().get("release_date").getAsString();
      String title = list.get(i).getAsJsonObject().get("title").getAsString();

      TmdbDto.TmdbMovie movie = TmdbDto.TmdbMovie.builder().poster_path(poster_path).release_date(release_date).title(title).build();
      tmdbMovies.add(movie);
    }
    return TmdbDto.builder().tmdbMovies(tmdbMovies).build();
  }
}
