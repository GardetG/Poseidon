package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.TradeService;
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
public class TradeController {

  @Autowired
  private TradeService tradeService;

  @RequestMapping("/trade/list")
  public String home(Model model) {
    model.addAttribute("trades", tradeService.findAll());
    return "trade/list";
  }

  @GetMapping("/trade/add")
  public String addUser(TradeDto tradeDto) {
    return "trade/add";
  }

  @PostMapping("/trade/validate")
  public String validate(@Valid TradeDto tradeDto, BindingResult result, Model model) {
    if (!result.hasErrors()) {
      tradeService.add(tradeDto);
      return "redirect:/trade/list";
    }
    return "trade/add";
  }

  @GetMapping("/trade/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    model.addAttribute("tradeDto", tradeService.findById(id));
    return "trade/update";
  }

  @PostMapping("/trade/update/{id}")
  public String updateTrade(@PathVariable("id") Integer id, @Valid TradeDto tradeDto,
                            BindingResult result, Model model) throws ResourceNotFoundException {
    if (!result.hasErrors()) {
      tradeDto.setTradeId(id);
      tradeService.update(tradeDto);
      return "redirect:/trade/list";
    }
    return "trade/update";
  }

  @GetMapping("/trade/delete/{id}")
  public String deleteTrade(@PathVariable("id") Integer id, Model model) {
    // TODO: Find Trade by Id and delete the Trade, return to Trade list
    return "redirect:/trade/list";
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFoundException(ResourceNotFoundException e,
                                                RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", e.getMessage());
    return "redirect:/trade/list";
  }

}
