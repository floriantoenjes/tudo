package com.floriantoenjes.tudo.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.floriantoenjes.tudo.TestUtils.getJwtToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser
public class UserAndContactRequestIntegrationTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void shouldNotAddContactWithoutContactRequest() throws Exception {
        mockMvc.perform(put("/api/v1/users/1/contacts")
                .header("Authorization", getJwtToken(mockMvc, "user", "password"))
                .contentType("text/uri-list")
                .content("/api/v1/users/4"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldAddContactWithContactRequest() throws Exception {
        mockMvc.perform(put("/api/v1/users/1/contacts")
                .header("Authorization", getJwtToken(mockMvc, "user", "password"))
                .contentType("text/uri-list")
                .content("/api/v1/users/5"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
