package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @RequestMapping("/user/list")
  public String home(Model model) {
    model.addAttribute("users", userService.findAll());
    return "user/list";
  }

  @GetMapping("/user/add")
  public String addUser(UserDto userDto) {
    return "user/add";
  }

  @PostMapping("/user/validate")
  public String validate(@Valid UserDto userDto, BindingResult result, Model model) {
    if (!result.hasErrors()) {
      userService.add(userDto);
      return "redirect:/user/list";
    }
    return "user/add";
  }

  @GetMapping("/user/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    model.addAttribute("userDto", userService.findById(id));
    return "user/update";
  }

  @PostMapping("/user/update/{id}")
  public String updateUser(@PathVariable("id") Integer id, @Valid UserDto userDto,
                           BindingResult result, Model model) throws ResourceNotFoundException {
    if (result.hasErrors()) {
      return "user/update";
    }
    userDto.setId(id);
    userService.update(userDto);
    model.addAttribute("users", userRepository.findAll());
    return "redirect:/user/list";
  }

  @GetMapping("/user/delete/{id}")
  public String deleteUser(@PathVariable("id") Integer id, Model model) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    userRepository.delete(user);
    model.addAttribute("users", userRepository.findAll());
    return "redirect:/user/list";
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFoundException(ResourceNotFoundException e,
                                                RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", e.getMessage());
    return "redirect:/user/list";
  }

}
