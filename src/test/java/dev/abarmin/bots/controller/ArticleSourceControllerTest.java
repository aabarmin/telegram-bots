package dev.abarmin.bots.controller;

import dev.abarmin.bots.repository.ArticleSourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArticleSourceController.class)
class ArticleSourceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleSourceRepository repository;

    @Test
    @WithMockUser
    void index_shouldCallRepository() throws Exception {
        mockMvc.perform(get("/telegram/article-sources"))
                .andExpect(status().isOk())
                .andExpect(view().name("telegram/article-sources/index"))
                .andExpect(model().attribute("sources", repository.findAll()));
    }
}