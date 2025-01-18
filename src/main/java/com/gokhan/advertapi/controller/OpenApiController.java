package com.gokhan.advertapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Hidden
@Controller
public class OpenApiController {

  @GetMapping({"", "/swagger", "/oas3"})
  public String redirectSwagger() {
    return "redirect:/swagger-ui.html";
  }
}

