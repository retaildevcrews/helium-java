package com.microsoft.azure.helium.app;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController {
    @RequestMapping("/")
    public String index() {
        return "redirect:swagger-ui.html";
    }

}