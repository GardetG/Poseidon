package com.nnk.springboot.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class implementing ErrorController for displaying custom error pages.
 */
@Controller
public class CustomErrorController implements ErrorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomErrorController.class);

  /**
   * Map the request error status and return the corresponding view.
   *
   * @param request with error status code
   * @param model of the View
   * @return View
   */
  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (status != null) {
      int statusCode = Integer.parseInt(status.toString());
      LOGGER.info("Error {}: An error occurred.", statusCode);
      if (statusCode == HttpStatus.NOT_FOUND.value()) {
        model.addAttribute("errorMsg", "The requested data are not found.");
        return "error/404";
      } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
        model.addAttribute("errorMsg", "You are not authorized for the requested data.");
        return "error/403";
      }
      model.addAttribute("errorCode", statusCode);
    }
    return "error/error";
  }

}
