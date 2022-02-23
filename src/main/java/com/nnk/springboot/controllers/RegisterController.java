package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceAlreadyExistsException;
import com.nnk.springboot.services.UserService;
import com.nnk.springboot.utils.UserMapper;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller Class for displaying and validate register form.
 */
@Controller
public class RegisterController {

  private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

  @Autowired
  private UserService userService;

  /**
   * Show the form view to register a user.
   *
   * @param userDto of the View
   * @return View
   */
  @GetMapping("/register")
  public String registerUser(UserDto userDto) {
    LOGGER.info("Request form to register User");
    userDto.setRole("USER");
    return "register";
  }

  /**
   * Validate the Register form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist User and return to home page.
   *
   * @param userDto of the form
   * @param result  hold validation errors
   * @param model of the View
   * @return View
   */
  @PostMapping("/register/validate")
  public String register(@Valid UserDto userDto, BindingResult result, Model model) {
    LOGGER.info("Request validation on user registration");
    if (!result.hasErrors()) {
      try {
        userService.add(userDto);
        LOGGER.info("User successfully registered");
        return "redirect:/";
      } catch (ResourceAlreadyExistsException e) {
        model.addAttribute("error", e.getMessage());
      }
    }
    LOGGER.info("Failed to register user, form contains errors");
    return "register";
  }

  /**
   * Show the OAuthRegister view to define a OAuth2User password.
   *
   * @param model of the View
   * @param user to update password
   * @return View
   */
  @GetMapping("/OAuthRegister")
  public String registerOAuthUser(Model model, @AuthenticationPrincipal User user) {
    if (user.getPassword() != null) {
      return "redirect:/bidList/list";
    }
    LOGGER.info("Request form to register OAuth2 user password");
    model.addAttribute("userDto", UserMapper.toDto(user));
    return "OAuthRegister";
  }

  /**
   * Validate the OAuthRegister form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist User and return to bidList page.
   *
   * @param userDto of the form
   * @param result  hold validation errors
   * @param model of the view
   * @return View
   */
  @PostMapping("/OAuthRegister/validate")
  public String registerValidation(@Valid UserDto userDto, BindingResult result, Model model) {
    LOGGER.info("Request validation on OAUth2 user password registration");
    if (!result.hasErrors()) {
      try {
        LOGGER.info("OAuth2 User password successfully registered");
        userService.update(userDto);
        return "redirect:/bidList/list";
      } catch (Exception e) {
        return "redirect:/";
      }
    }
    LOGGER.info("Failed to register OAuth2 User password, form contains errors");
    return "OAuthRegister";
  }

}
