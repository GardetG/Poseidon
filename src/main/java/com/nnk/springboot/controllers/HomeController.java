package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class for displaying home page view.
 */
@Controller
public class HomeController {

  @RequestMapping("/")
  public String home() {
    return "home";
  }

}
