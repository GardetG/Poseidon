package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceAlreadyExistsException;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.UserService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller class for displaying thymeleaf view of User management.
 */
@Controller
public class UserController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;

  /**
   * Show a view displaying all the Users.
   *
   * @param model of the view
   * @return View
   */
  @RequestMapping("/user/list")
  public String home(Model model) {
    LOGGER.info("Request list of all Users");
    model.addAttribute("users", userService.findAll());
    return "user/list";
  }

  /**
   * Show a view with the add User form.
   *
   * @param userDto of the form
   * @return View
   */
  @GetMapping("/user/add")
  public String addUser(UserDto userDto) {
    LOGGER.info("Request add User form");
    return "user/add";
  }

  /**
   * Validate the add User form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist User and return the User list view.
   *
   * @param userDto of the form
   * @param result  hold validation errors
   * @return View
   */
  @PostMapping("/user/validate")
  public String validate(@Valid UserDto userDto, BindingResult result, Model model) {
    LOGGER.info("Request add User form validation");
    if (!result.hasErrors()) {
      try {
        userService.add(userDto);
        LOGGER.info("User successfully added");
        return "redirect:/user/list";
      } catch (ResourceAlreadyExistsException e) {
        model.addAttribute("error", e.getMessage());
      }
    }
    LOGGER.info("Failed to add User, form contains errors");
    return "user/add";
  }

  /**
   * Show a view with update User form prefilled with User current values.
   *
   * @param id    of the User to update
   * @param model of the view
   * @return View
   * @throws ResourceNotFoundException when the requested User not found
   */
  @GetMapping("/user/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    LOGGER.info("Request update User id {} form", id);
    model.addAttribute("userDto", userService.findById(id));
    return "user/update";
  }

  /**
   * Validate the update User form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist User and return to User list view.
   *
   * @param id      of the User to update
   * @param userDto of the form
   * @param result  hold validation errors
   * @param model of he View
   * @return View
   * @throws ResourceNotFoundException when the requested User not found
   */
  @PostMapping("/user/update/{id}")
  public String updateUser(@PathVariable("id") Integer id, @Valid UserDto userDto,
                           BindingResult result, Model model) throws ResourceNotFoundException {
    LOGGER.info("Request update User id {} form validation", id);
    if (!result.hasErrors()) {
      try {
        userDto.setId(id);
        userService.update(userDto);
        LOGGER.info("User id {} successfully updated", id);
        return "redirect:/user/list";
      } catch (ResourceAlreadyExistsException e) {
        model.addAttribute("error", e.getMessage());
      }
    }
    LOGGER.info("Failed to update User id {}, form contains errors", id);
    return "user/update";
  }

  /**
   * Request the deletion of a User and return to User list view.
   *
   * @param id of the User to delete
   * @return View
   * @throws ResourceNotFoundException when the requested User not found
   */
  @GetMapping("/user/delete/{id}")
  public String deleteUser(@PathVariable("id") Integer id)
      throws ResourceNotFoundException {
    LOGGER.info("Request User id {} deletion", id);
    userService.delete(id);
    LOGGER.info("User id {} successfully deleted", id);
    return "redirect:/user/list";
  }

  /**
   * Handle ResourceNotFoundException and redirect to User list view with an error message.
   *
   * @param e                  exception handled
   * @param redirectAttributes to add message to the view
   * @return View
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFoundException(ResourceNotFoundException e,
                                                RedirectAttributes redirectAttributes) {
    LOGGER.info(e.getMessage());
    redirectAttributes.addFlashAttribute("error", e.getMessage());
    return "redirect:/user/list";
  }

}
