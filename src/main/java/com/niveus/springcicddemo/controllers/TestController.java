package com.niveus.springcicddemo.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	
	
  @GetMapping("/version1")
  public String getVersion1() {
    return "Version 1.0";
  }
  
  @GetMapping("/version2")
  public String getVersion2() {
    return "Version 2.0";
  }
  
  @GetMapping("/version3")
  public String getVersion3() {
    return "Version 3.0";
  }
  
  @GetMapping("/version4")
  public String getVersion4() {
    return "Version 4.0";
  }
  
  @GetMapping("/version5")
  public String getVersion5() {
    return "Version 5.0 - This is Canary deployment";
  }
  
}
