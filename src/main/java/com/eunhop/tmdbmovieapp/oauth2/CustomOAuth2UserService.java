package com.eunhop.tmdbmovieapp.oauth2;

import com.eunhop.tmdbmovieapp.domain.OAuth2;
import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.dto.security.PrincipalUser;
import com.eunhop.tmdbmovieapp.repository.OAuth2Repository;
import com.eunhop.tmdbmovieapp.repository.UserRepository;
import com.eunhop.tmdbmovieapp.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;
  private final OAuth2Repository oAuth2Repository;
  private final OAuth2Service oAuth2Service;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    final DefaultOAuth2UserService userService = new DefaultOAuth2UserService();
    final OAuth2User oAuth2User = userService.loadUser(userRequest);
    String provider = userRequest.getClientRegistration().getRegistrationId();

    if (provider.equals("naver")) {
      Map<String, Object> attributesResponse = (Map<String, Object>) oAuth2User.getAttributes().get("response");
      String resEmail = attributesResponse.get("email").toString();
      String resName = attributesResponse.get("name").toString();

      oAuth2Service.userAndOAuth2DBSave(resEmail, resName, provider);

      return new PrincipalUser(
          oAuth2Service.findUser(resEmail),
          attributesResponse
      );
    } else {
      Map<String, Object> attributesResponse = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
      Map<String, Object> profileResponse = (Map<String, Object>) attributesResponse.get("profile");
      String resNickname = profileResponse.get("nickname").toString();
      String resEmail = attributesResponse.get("email").toString();

      oAuth2Service.userAndOAuth2DBSave(resEmail, resNickname, provider);

      return new PrincipalUser(
          oAuth2Service.findUser(resEmail),
          attributesResponse
      );
    }
  }

}
