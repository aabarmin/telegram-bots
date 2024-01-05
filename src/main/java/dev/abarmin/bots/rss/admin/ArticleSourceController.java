package dev.abarmin.bots.rss.admin;

import dev.abarmin.bots.rss.reader.persistence.ArticleSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ArticleSourceController {
    private final ArticleSourceRepository repository;

    @GetMapping("/telegram/article-sources")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.addObject("sources", repository.findAll());
        modelAndView.setViewName("telegram/article-sources/index");
        return modelAndView;
    }
}
