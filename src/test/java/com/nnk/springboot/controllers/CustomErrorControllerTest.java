package com.nnk.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.servlet.ModelAndView;

@WebMvcTest(CustomErrorController.class)
class CustomErrorControllerTest {

  @Autowired
  private CustomErrorController customErrorController;

  @MockBean
  private HttpServletRequest request;
  @MockBean
  private UserDetailsService userDetailsService;
  @MockBean
  private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

  @DisplayName("Handled error status should return corresponding view with error message")
  @ParameterizedTest(name = "{index} Error {0} return error/{0}")
  @ValueSource(ints = {404, 403})
  void handleErrorTest(int code) {
    // GIVEN
    when(request.getAttribute(anyString())).thenReturn(code);

    // WHEN
    ModelAndView actualMav = customErrorController.handleError(request);

    // THEN
    assertThat(actualMav.getViewName()).isEqualTo("error/"+code);
    assertThat(actualMav.getModelMap().containsAttribute("errorMsg")).isTrue();
  }

  @DisplayName("Other error status should return error view with error code")
  @Test
  void handleOtherErrorTest() {
    // GIVEN
    when(request.getAttribute(anyString())).thenReturn(500);

    // WHEN
    ModelAndView actualMav = customErrorController.handleError(request);

    // THEN
    assertThat(actualMav.getViewName()).isEqualTo("error/error");
    assertThat(actualMav.getModelMap().containsAttribute("errorCode")).isTrue();
  }

  @DisplayName("Null error status should return error view")
  @Test
  void handleNullErrorTest() {
    // GIVEN
    when(request.getAttribute(anyString())).thenReturn(null);

    // WHEN
    ModelAndView actualMav = customErrorController.handleError(request);

    // THEN
    assertThat(actualMav.getViewName()).isEqualTo("error/error");
  }

}
