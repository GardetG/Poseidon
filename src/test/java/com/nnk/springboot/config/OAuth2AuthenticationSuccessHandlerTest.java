package com.nnk.springboot.config;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.User;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

@SpringBootTest
class OAuth2AuthenticationSuccessHandlerTest {

  @Autowired
  private OAuth2AuthenticationSuccessHandler successHandler;

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private Authentication authentication;

  @DisplayName("Successfully OAuth2 authentication with password define should redirect on bidList view")
  @Test
  void onAuthenticationSuccessWithPasswordDefinetest() throws IOException {
    // GIVEN
    User user = new User("Username", "Password", "Fullname", "USER");
    when(authentication.getPrincipal()).thenReturn(user);

    // WHEN
    successHandler.onAuthenticationSuccess(request, response, authentication);

    // THEN
    verify(response, times(1)).sendRedirect("/bidList/list");
  }

  @DisplayName("Successfully OAuth2 authentication without password define should redirect on OAuthRegister view")
  @Test
  void onAuthenticationSuccessWithoutPasswordDefinetest() throws IOException {
    // GIVEN
    User user = new User("Username", null, "Fullname", "USER");
    when(authentication.getPrincipal()).thenReturn(user);

    // WHEN
    successHandler.onAuthenticationSuccess(request, response, authentication);

    // THEN
    verify(response, times(1)).sendRedirect("/OAuthRegister");
  }

}
