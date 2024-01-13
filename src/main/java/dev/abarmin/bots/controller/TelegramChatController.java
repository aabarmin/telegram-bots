package dev.abarmin.bots.controller;

import dev.abarmin.bots.rss.persistence.TelegramBotChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class TelegramChatController {
    private final TelegramBotChatRepository repository;

    @GetMapping("/telegram/chats")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.addObject("chats", repository.findAll());
        modelAndView.setViewName("telegram/chats/index");
        return modelAndView;
    }
}
