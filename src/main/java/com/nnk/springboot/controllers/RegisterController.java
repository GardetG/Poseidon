package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceAlreadyExistsException;
import com.nnk.springboot.services.UserService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  @GetMapping("/register")
  public String registerUser(UserDto userDto) {
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
   * @return View
   */
  @PostMapping("/register/validate")
  public String register(@Valid UserDto userDto, BindingResult result, Model model) {
    LOGGER.info("Request register form validation");
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

}
