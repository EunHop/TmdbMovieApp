package com.eunhop.tmdbmovieapp.oauth2;

import com.eunhop.tmdbmovieapp.domain.OAuth2;
import com.eunhop.tmdbmovieapp.domain.Roles;
import com.eunhop.tmdbmovieapp.domain.User;
import com.eunhop.tmdbmovieapp.dto.security.PrincipalUser;
import com.eunhop.tmdbmovieapp.repository.OAuth2Repository;
import com.eunhop.tmdbmovieapp.repository.UserRepository;
import com.eunhop.tmdbmovieapp.service.OAuth2Service;
import com.eunhop.tmdbmovieapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class GoogleOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

  private final UserRepository userRepository;
  private final OAuth2Service oAuth2Service;

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    final OidcUserService oidcUserService = new OidcUserService();
    final OidcUser oidcUser = oidcUserService.loadUser(userRequest);

    String provider = userRequest.getClientRegistration().getRegistrationId();
    String resName = oidcUser.getAttributes().get("name").toString();
    String resEmail = oidcUser.getAttributes().get("email").toString();

    oAuth2Service.userAndOAuth2DBSave(resEmail, resName, provider);

    return new PrincipalUser(
        oAuth2Service.findUser(resEmail),
        oidcUser.getAttributes()
    );
  }
}
