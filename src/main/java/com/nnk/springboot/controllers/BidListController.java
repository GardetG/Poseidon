package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.services.BidListService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
  public String validate(@Valid BidListDto bidListDto, BindingResult result, Model model) {
    if (!result.hasErrors()) {
      bidListService.add(bidListDto);
      model.addAttribute("bidLists", bidListService.findAll());
      return "redirect:/bidList/list";
    }
    return "bidList/add";
  }

  @GetMapping("/bidList/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    // TODO: get Bid by Id and to model then show to the form
    return "bidList/update";
  }

  @PostMapping("/bidList/update/{id}")
  public String updateBid(@PathVariable("id") Integer id, @Valid BidListDto bidListDto,
                          BindingResult result, Model model) {
    // TODO: check required fields, if valid call service to update Bid and return list Bid
    return "redirect:/bidList/list";
  }

  @GetMapping("/bidList/delete/{id}")
  public String deleteBid(@PathVariable("id") Integer id, Model model) {
    // TODO: Find Bid by Id and delete the bid, return to Bid list
    return "redirect:/bidList/list";
  }
}
