package dev.abarmin.bots.controller;

import dev.abarmin.bots.service.updates.UpdatesSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class ToolsController {
    private final UpdatesSender updatesSender;

    @GetMapping("/telegram/tools")
    public ModelAndView toolsView(ModelAndView modelAndView) {
        modelAndView.setViewName("telegram/tools/index");
        return modelAndView;
    }

    @GetMapping("/telegram/tools/digest")
    public RedirectView sendDigest() {
        updatesSender.sendUpdates();
        return new RedirectView("/telegram/tools");
    }
}
