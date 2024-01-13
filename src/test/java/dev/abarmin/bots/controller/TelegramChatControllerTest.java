package dev.abarmin.bots.controller;

import dev.abarmin.bots.rss.persistence.TelegramBotChatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TelegramChatController.class)
class TelegramChatControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TelegramBotChatRepository repository;

    @Test
    @WithMockUser
    void index_shouldCallRepository() throws Exception {
        mockMvc.perform(get("/telegram/chats"))
                .andExpect(status().isOk())
                .andExpect(view().name("telegram/chats/index"))
                .andExpect(model().attribute("chats", repository.findAll()));
    }
}