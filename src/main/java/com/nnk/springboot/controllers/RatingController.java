package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.RatingService;
import javax.validation.Valid;
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

@Controller
public class RatingController {

  @Autowired
  private RatingService ratingService;

  @RequestMapping("/rating/list")
  public String home(Model model) {
    model.addAttribute("ratings", ratingService.findAll());
    return "rating/list";
  }

  @GetMapping("/rating/add")
  public String addRatingForm(RatingDto ratingDto) {
    return "rating/add";
  }

  @PostMapping("/rating/validate")
  public String validate(@Valid RatingDto ratingDto, BindingResult result, Model model) {
    if (!result.hasErrors()) {
      ratingService.add(ratingDto);
      return "redirect:/rating/list";
    }
    return "rating/add";
  }

  @GetMapping("/rating/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    model.addAttribute("ratingDto", ratingService.findById(id));
    return "rating/update";
  }

  @PostMapping("/rating/update/{id}")
  public String updateRating(@PathVariable("id") Integer id, @Valid RatingDto ratingDto,
                             BindingResult result, Model model) throws ResourceNotFoundException {
    if (!result.hasErrors()) {
      ratingDto.setId(id);
      ratingService.update(ratingDto);
      return "redirect:/rating/list";
    }
    return "rating/update";
  }

  @GetMapping("/rating/delete/{id}")
  public String deleteRating(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    ratingService.delete(id);
    return "redirect:/rating/list";
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFoundException(ResourceNotFoundException e,
                                                RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", e.getMessage());
    return "redirect:/rating/list";
  }

}
