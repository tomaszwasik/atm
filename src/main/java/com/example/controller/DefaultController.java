package com.example.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
@FunctionalInterface
public interface DefaultController {

    @GetMapping
    String showPage(HttpServletRequest request, Model model);
}
