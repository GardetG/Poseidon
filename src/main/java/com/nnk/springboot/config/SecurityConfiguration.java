package com.nnk.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Configuration class for Spring Security Authentication and Authorization.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

  /**
   * Configure AuthenticationManagerBuilder to use userDetailService with passwordEncoder to
   * authenticate user with persisted credentials in database.
   *
   * @param auth AuthenticationManagerBuilder
   * @throws Exception when Authentication failed
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  /**
   * Configure HttpSecurity filters-chain with authorization by roles, loginform, logout, and
   * OAuth2.
   *
   * @param http HttpSecurity
   * @throws Exception When Authentication failed
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
          .antMatchers("/css/**", "/img/**").permitAll()
          .antMatchers("/", "/register/**").permitAll()
          .antMatchers("/user/**").hasRole("ADMIN")
          .anyRequest().authenticated()
        .and()
          .formLogin()
          .loginProcessingUrl("/login")
          .defaultSuccessUrl("/bidList/list", true)
        .and()
          .logout()
          .logoutUrl("/app-logout")
          .logoutSuccessUrl("/")
        .and()
          .oauth2Login()
          .userInfoEndpoint()
          .userService(oAuth2UserService)
          .and()
          .successHandler(new OAuth2AuthenticationSuccessHandler());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
