package dev.abarmin.bots.controller;

import dev.abarmin.bots.entity.episodes.Episode;
import dev.abarmin.bots.repository.EpisodesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class EpisodesController {
    private final EpisodesRepository repository;

    @GetMapping("/episodes")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.addObject("episodes", repository.findAll());
        modelAndView.setViewName("episodes/index");
        return modelAndView;
    }

    @GetMapping("/episodes/add")
    public ModelAndView add(ModelAndView modelAndView) {
        modelAndView.addObject("episode", new Episode());
        modelAndView.setViewName("episodes/edit");
        return modelAndView;
    }

    @PostMapping("/episodes")
    public RedirectView save(Episode episode) {
        repository.save(episode);
        return new RedirectView("/episodes");
    }
}
