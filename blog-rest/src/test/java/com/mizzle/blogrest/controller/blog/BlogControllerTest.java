package com.mizzle.blogrest.controller.blog;

import com.mizzle.blogrest.service.blog.BoardService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;

import static org.mockito.ArgumentMatchers.any; 
import static org.mockito.BDDMockito.given; 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; 
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "local")
public class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private BoardService boardService;

    @Test
    void testCreateBoard() {
        
    }

    @Test
    void testCreateLike() {

    }

    @Test
    void testCreateReply() {

    }

    @Test
    void testDeleteBoard() {

    }

    @Test
    void testDeleteLike() {

    }

    @Test
    void testReadBoard() throws Exception {
                    
    }

    @Test
    void testReadBoards() throws Exception {
        this.mockMvc.perform(
                        get("/blog/board")
                        .param("search", "")
                        .param("page", "0")
                        .param("size","20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(print());

    }

    @Test
    void testReadReplys() {

    }

    @Test
    void testUpdateBoard() {

    }

    @Test
    void testUpdateReply() {

    }
}
