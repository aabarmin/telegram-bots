package dev.abarmin.bots.controller;

import dev.abarmin.bots.service.DuplicatesFinderAndResolver;
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
    private final DuplicatesFinderAndResolver duplicatesFinderAndResolver;

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

    @GetMapping("/telegram/tools/duplicates")
    public String findDuplicates() {
        duplicatesFinderAndResolver.findAndResolveDuplicates();
        return "redirect:/telegram/tools";
    }
}
