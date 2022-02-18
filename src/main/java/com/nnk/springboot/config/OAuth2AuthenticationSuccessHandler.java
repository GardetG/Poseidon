package com.nnk.springboot.config;

import com.nnk.springboot.domain.User;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * AuthenticationSuccessHandler for OAuth2 Authentication to redirect user with no password defined
 * to OAuthRegister form or pursue on bidList page.
 */
@Configuration
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse,
                                      Authentication authentication) throws IOException {
    User user = (User) authentication.getPrincipal();
    if (user.getPassword() == null) {
      httpServletResponse.sendRedirect("/OAuthRegister");
      return;
    }
    httpServletResponse.sendRedirect("/bidList/list");
  }

}

