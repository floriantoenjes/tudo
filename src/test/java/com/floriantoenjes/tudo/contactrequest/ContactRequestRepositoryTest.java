package com.floriantoenjes.tudo.contactrequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser
public class ContactRequestRepositoryTest {
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void findOneWithWrongUserShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/contactRequests/1").with(httpBasic("user3", "password")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void findOneWithCorrectUserShouldReturnContactRequest() throws Exception {
        mockMvc.perform(get("/api/v1/contactRequests/1").with(httpBasic("user2", "password")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}