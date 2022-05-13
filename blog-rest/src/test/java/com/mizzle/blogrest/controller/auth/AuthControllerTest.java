package com.mizzle.blogrest.controller.auth;

import com.mizzle.blogrest.payload.request.auth.SignUpRequest;
import com.mizzle.blogrest.service.auth.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any; 
import static org.mockito.BDDMockito.given; 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
//@SpringBootTest
public class AuthControllerTest {
    

    private MockMvc mockMvc;

    @MockBean
    private AuthService authService; 

    @Test
    void testDelete() {

    }

    @Test
    void testModify() {

    }

    @Test
    void testRefresh() {

    }

    @Test
    void testSignin() throws Exception {

    }

    @Test
    void testSignout() {
        
    }

    @Test
    void testSignup() {

    }

    @Test
    void testWhoAmI() {

    }
}
