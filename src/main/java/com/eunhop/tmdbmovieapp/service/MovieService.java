package com.eunhop.tmdbmovieapp.service;

import com.eunhop.tmdbmovieapp.dto.TmdbDto;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
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

  public TmdbDto popular(String media) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.themoviedb.org/3/trending/all/day?language=ko-KR"))
        .header("accept", "application/json")
        .header("Authorization", "Bearer " + TMDB_API_KEY)
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
    return duplicatedMethod(request, media);
  }
  public TmdbDto search(String query, String media, int page) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.themoviedb.org/3/search/"+media+"?query="+query+"&language=ko-KR&page="+page))
        .header("accept", "application/json")
        .header("Authorization", "Bearer " + TMDB_API_KEY)
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
    return duplicatedMethod(request, media);
  }

  public TmdbDto.TmdbMovie details(String media, int movie_id) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.themoviedb.org/3/"+media+"/"+movie_id+"?language=ko-KR"))
        .header("accept", "application/json")
        .header("Authorization", "Bearer " + TMDB_API_KEY)
        .method("GET", HttpRequest.BodyPublishers.noBody())
        .build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    JsonObject first = JsonParser.parseString(response.body()).getAsJsonObject();
      String first_air_date = null;
      String release_date = null;
      String name = null;
      String title = null;
      Integer budget = null;
      int tmdb_movie_id = first.get("id").getAsInt();
      String poster_path = first.get("poster_path").isJsonNull() ? null : first.get("poster_path").getAsString();
      if (first.get("release_date")!=null) {
        release_date = first.get("release_date").isJsonNull() ? null : first.get("release_date").getAsString();
      } else if(first.get("first_air_date")!=null){
        first_air_date = first.get("first_air_date").isJsonNull() ? null : first.get("first_air_date").getAsString();
      }
      if(first.get("title")!=null) {
        title = first.get("title").isJsonNull() ? null : first.get("title").getAsString();
      } else if(first.get("name")!=null){
        name = first.get("name").isJsonNull() ? null : first.get("name").getAsString();
      }
      String backdrop_path = first.get("backdrop_path").isJsonNull() ? null : first.get("backdrop_path").getAsString();
      if(first.get("budget")!=null) {
        budget = first.get("budget").isJsonNull() ? null : first.get("budget").getAsInt();
      }
      List<String> genres = new ArrayList<>();
      for(int i=0; i<first.getAsJsonArray("genres").size(); i++) {
        genres.add(first.getAsJsonArray("genres").get(i).getAsJsonObject().get("name").getAsString());
      }
      String overview = first.get("overview").isJsonNull() ? null : first.get("overview").getAsString();
      String tagline = first.get("tagline").isJsonNull() ? null : first.get("tagline").getAsString();
      Float vote_average = first.get("vote_average").isJsonNull() ? null : first.get("vote_average").getAsFloat();
      return TmdbDto.TmdbMovie.builder()
          .tmdb_movie_id(tmdb_movie_id).poster_path(poster_path)
          .release_date(release_date).first_air_date(first_air_date)
          .title(title).name(name).backdrop_path(backdrop_path).budget(budget)
          .genres(genres).overview(overview).tagline(tagline)
          .vote_average(vote_average).build();
  }

  public TmdbDto duplicatedMethod(HttpRequest request, String media) throws IOException, InterruptedException {
    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    JsonObject first = JsonParser.parseString(response.body()).getAsJsonObject();
    int page = first.get("page").getAsInt();
    int total_pages = first.get("total_pages").getAsInt();
    int total_results = first.get("total_results").getAsInt();
    JsonArray list = first.getAsJsonArray("results");
    List<TmdbDto.TmdbMovie> tmdbMovies = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      String first_air_date = null;
      String release_date = null;
      String name = null;
      String title = null;
      String media_type = null;
      int tmdb_movie_id = list.get(i).getAsJsonObject().get("id").getAsInt();
      String poster_path = list.get(i).getAsJsonObject().get("poster_path").isJsonNull() ? null : list.get(i).getAsJsonObject().get("poster_path").getAsString();
      if (list.get(i).getAsJsonObject().get("release_date")!=null) {
        release_date = list.get(i).getAsJsonObject().get("release_date").isJsonNull() ? null : list.get(i).getAsJsonObject().get("release_date").getAsString();
      } else if(list.get(i).getAsJsonObject().get("first_air_date")!=null){
        first_air_date = list.get(i).getAsJsonObject().get("first_air_date").isJsonNull() ? null : list.get(i).getAsJsonObject().get("first_air_date").getAsString();
      }
      if(list.get(i).getAsJsonObject().get("title")!=null) {
        title = list.get(i).getAsJsonObject().get("title").isJsonNull() ? null : list.get(i).getAsJsonObject().get("title").getAsString();
      } else if(list.get(i).getAsJsonObject().get("name")!=null){
        name = list.get(i).getAsJsonObject().get("name").isJsonNull() ? null : list.get(i).getAsJsonObject().get("name").getAsString();
      }
      if(list.get(i).getAsJsonObject().get("media_type")!=null) {
        media_type = list.get(i).getAsJsonObject().get("media_type").isJsonNull() ? null : list.get(i).getAsJsonObject().get("media_type").getAsString();
      } else {
        media_type = media;
      }
      TmdbDto.TmdbMovie movie = TmdbDto.TmdbMovie.builder()
          .tmdb_movie_id(tmdb_movie_id).poster_path(poster_path)
          .release_date(release_date).first_air_date(first_air_date)
          .title(title).name(name).media_type(media_type).build();
      tmdbMovies.add(movie);
    }
    return TmdbDto.builder().page(page).total_pages(total_pages).total_results(total_results).tmdbMovies(tmdbMovies).build();
  }
}
