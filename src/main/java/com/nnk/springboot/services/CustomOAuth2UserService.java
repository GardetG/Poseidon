package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Service Class implementing extending DefaultOAuth2UserService for OAuth2 authentication.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    return loadUserFromRepository(oAuth2User);
  }

  /**
   * Return a User entity with OAuth2User attributes and persisted data by username if already
   * register. If the user is not yet persisted, persist a new one base on OAuth2User data, with
   * USER role and a to defined null password.
   *
   * @param oAuth2User from OAuth2 authentication
   * @return User entity
   */
  protected User loadUserFromRepository(OAuth2User oAuth2User) {
    User user = userRepository.findByUsername(oAuth2User.getAttribute("login"))
        .orElseGet(() -> registerOAuth2User(oAuth2User));
    user.setAttributes(oAuth2User.getAttributes());
    return user;
  }

  private User registerOAuth2User(OAuth2User oAuth2User) {
    User user = new User(
        oAuth2User.getAttribute("login"),
        null,
        oAuth2User.getAttribute("name"),
        "USER"
    );
    return userRepository.save(user);
  }

}