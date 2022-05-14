package com.mizzle.blogrest.controller.blog;

import com.mizzle.blogrest.domain.entity.blog.Purpose;
import com.mizzle.blogrest.lib.JsonUtils;
import com.mizzle.blogrest.payload.request.auth.SignInRequest;
import com.mizzle.blogrest.payload.request.blog.CreateBoardRequest;
import com.mizzle.blogrest.payload.request.blog.CreateReplyRequest;
import com.mizzle.blogrest.service.blog.BoardService;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

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

    private JSONObject signin(String email) throws Exception{
        
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail(email);
        signInRequest.setPassword("string");

        ResultActions actions = this.mockMvc.perform(
                    post("/auth/signin")
                    .content(
                        JsonUtils.asJsonToString(signInRequest)
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        JSONObject jsonObject = JsonUtils.asStringToJson(actions.andReturn().getResponse().getContentAsString());
        return jsonObject;
    }

    private JSONObject createBoard() throws Exception {
        //give
        JSONObject token = signin("string@aa.bb");
                
        String accessToken = (String) token.get("accessToken");

        List<String> tagNames = new ArrayList<>();
        tagNames.add("tag1");
        tagNames.add("tag2");

        CreateBoardRequest createBoardRequest = CreateBoardRequest.builder()
                                                                .title("sample")
                                                                .subtitle("sample")
                                                                .purpose(Purpose.finish)
                                                                .markdown("markdown")
                                                                .html("<p>markdown</p>")
                                                                .tagNames(tagNames)
                                                                .build();

        //when
        ResultActions actions = this.mockMvc.perform(
                    post("/blog/board")
                    .content(
                        JsonUtils.asJsonToString(createBoardRequest)
                    )
                    .header("Authorization", String.format("Bearer %s", accessToken))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        JSONObject jsonObject = JsonUtils.asStringToJson(actions.andReturn().getResponse().getContentAsString());
        return (JSONObject) jsonObject.get("information");
        
    }

    @Test
    void testCreateBoard() throws Exception {
        //give
        JSONObject token = signin("string@aa.bb");
        
        String accessToken = (String) token.get("accessToken");

        List<String> tagNames = new ArrayList<>();
        tagNames.add("tag1");
        tagNames.add("tag2");

        CreateBoardRequest createBoardRequest = CreateBoardRequest.builder()
                                                                .title("sample")
                                                                .subtitle("sample")
                                                                .purpose(Purpose.finish)
                                                                .markdown("markdown")
                                                                .html("<p>markdown</p>")
                                                                .tagNames(tagNames)
                                                                .build();

        //when
        ResultActions actions = this.mockMvc.perform(
                    post("/blog/board")
                    .content(
                        JsonUtils.asJsonToString(createBoardRequest)
                    )
                    .header("Authorization", String.format("Bearer %s", accessToken))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        JSONObject jsonObject = JsonUtils.asStringToJson(actions.andReturn().getResponse().getContentAsString());
        log.info("jsonObject={}",jsonObject);

    }

    @Test
    void testCreateLike() throws Exception {
        //give
        JSONObject token = signin("string@aa.bb");
        
        String accessToken = (String) token.get("accessToken");

        long boardId = 1;


        //when
        ResultActions actions = this.mockMvc.perform(
                    post(String.format("/blog/like/%d", boardId))
                    .header("Authorization", String.format("Bearer %s", accessToken))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        JSONObject jsonObject = JsonUtils.asStringToJson(actions.andReturn().getResponse().getContentAsString());
        log.info("jsonObject={}",jsonObject);
    }

    @Test
    void testCreateReply() throws Exception{
        //give
        JSONObject token = signin("string@aa.bb");
                
        String accessToken = (String) token.get("accessToken");

        long boardId = 1;

        CreateReplyRequest createReplyRequest = new CreateReplyRequest();
        createReplyRequest.setComment("testCreateLike");

        //when
        ResultActions actions = this.mockMvc.perform(
                    post(String.format("/blog/reply/%d", boardId))
                    .content(
                        JsonUtils.asJsonToString(createReplyRequest)
                    )
                    .header("Authorization", String.format("Bearer %s", accessToken))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        JSONObject jsonObject = JsonUtils.asStringToJson(actions.andReturn().getResponse().getContentAsString());
        log.info("jsonObject={}",jsonObject);
    }

    @Test
    void testDeleteBoard() throws Exception {
        JSONObject board = createBoard();
        long boardId =  (long) board.get("id");
        
        JSONObject token = signin("string@aa.bb");
        String accessToken = (String) token.get("accessToken");

        //when
        ResultActions actions = this.mockMvc.perform(
                    delete(String.format("/blog/board/%d", boardId))
                    .header("Authorization", String.format("Bearer %s", accessToken))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
        //then
        JSONObject jsonObject = JsonUtils.asStringToJson(actions.andReturn().getResponse().getContentAsString());
        log.info("jsonObject={}",jsonObject);
    }

    @Test
    void testDeleteLike() throws Exception {
        //give
        JSONObject token = signin("string@aa.bb");
        
        String accessToken = (String) token.get("accessToken");

        long boardId = 1;


        //when
        ResultActions actions = this.mockMvc.perform(
                    delete(String.format("/blog/like/%d", boardId))
                    .header("Authorization", String.format("Bearer %s", accessToken))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        JSONObject jsonObject = JsonUtils.asStringToJson(actions.andReturn().getResponse().getContentAsString());
        log.info("jsonObject={}",jsonObject);
    }

    @Test
    void testReadBoard() throws Exception {
        //give
        long boardId = 1;
        //when
        
        ResultActions actions = this.mockMvc.perform(
                        get(String.format("/blog/board/%d", boardId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(print());

        //then
        JSONObject jsonObject = JsonUtils.asStringToJson(actions.andReturn().getResponse().getContentAsString());
        log.info("jsonObject={}",jsonObject);

    }

    @Test
    void testReadBoards() throws Exception {

        //give
        
        //when
        ResultActions actions = this.mockMvc.perform(
                        get("/blog/board")
                        .param("search", "")
                        .param("page", "0")
                        .param("size","20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(print());

        //then
        JSONObject jsonObject = JsonUtils.asStringToJson(actions.andReturn().getResponse().getContentAsString());
        log.info("jsonObject={}",jsonObject);

    }

    @Test
    void testReadReplys() throws Exception {
        //give
        long boardId = 1;
        //when
                
        ResultActions actions = this.mockMvc.perform(
                    get(String.format("/blog/board/%d", boardId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        JSONObject jsonObject = JsonUtils.asStringToJson(actions.andReturn().getResponse().getContentAsString());
        log.info("jsonObject={}",jsonObject);
    }

    @Test
    void testUpdateBoard() {

    }

    @Test
    void testUpdateReply() {

    }
}
