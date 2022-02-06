package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.BidListService;
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

/**
 * Controller class for displaying thymeleaf view of BidList management.
 */
@Controller
public class BidListController {

  @Autowired
  BidListService bidListService;

  @RequestMapping("/bidList/list")
  public String home(Model model) {
    model.addAttribute("bidLists", bidListService.findAll());
    return "bidList/list";
  }

  @GetMapping("/bidList/add")
  public String addBidForm(BidListDto bidListDto) {
    return "bidList/add";
  }

  @PostMapping("/bidList/validate")
  public String validate(@Valid BidListDto bidListDto, BindingResult result) {
    if (!result.hasErrors()) {
      bidListService.add(bidListDto);
      return "redirect:/bidList/list";
    }
    return "bidList/add";
  }

  @GetMapping("/bidList/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    model.addAttribute("bidListDto", bidListService.findById(id));
    return "bidList/update";
  }

  @PostMapping("/bidList/update/{id}")
  public String updateBid(@PathVariable("id") Integer id, @Valid BidListDto bidListDto,
                          BindingResult result)
      throws ResourceNotFoundException {
    if (!result.hasErrors()) {
      bidListDto.setBidListId(id);
      bidListService.update(bidListDto);
      return "redirect:/bidList/list";
    }
    return "bidList/update";
  }

  @GetMapping("/bidList/delete/{id}")
  public String deleteBid(@PathVariable("id") Integer id)
      throws ResourceNotFoundException {
    bidListService.delete(id);
    return "redirect:/bidList/list";
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFoundException(ResourceNotFoundException e,
                                                RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", e.getMessage());
    return "redirect:/bidList/list";
  }
}
