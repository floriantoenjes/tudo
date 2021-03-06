package com.floriantoenjes.tudo.contactrequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.floriantoenjes.tudo.TestUtils.getJwtToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
        mockMvc.perform(get("/api/v1/contactRequests/13")
        .header("Authorization", getJwtToken(mockMvc, "user3", "password")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void findOneWithCorrectUserShouldReturnContactRequest() throws Exception {
        mockMvc.perform(get("/api/v1/contactRequests/13")
                .header("Authorization", getJwtToken(mockMvc, "user2", "password")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteWithWrongUserShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/contactRequests/13")
                .header("Authorization", getJwtToken(mockMvc, "user3", "password")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteWithCorrectUserShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/contactRequests/13")
                .header("Authorization", getJwtToken(mockMvc, "user", "password")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void findAllWithUserRoleShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/contactRequests")
                .header("Authorization", getJwtToken(mockMvc, "user", "password")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void findAllBySenderIdAndReceiverIdWithWrongUserShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/contactRequests/search/findBySenderUsernameAndReceiverUsername?senderName=1&receiverName=2")
                .header("Authorization", getJwtToken(mockMvc, "user3", "password")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void findAllBySenderIdAndReceiverIdWithCorrectUserShouldReturnContactRequests() throws Exception {
        //    ToDo: Investigate why this test fails with 'user' instead of 'user2'
        mockMvc.perform(get("/api/v1/contactRequests/search/findBySenderUsernameAndReceiverUsername?senderName=user&receiverName=user2")
                .header("Authorization", getJwtToken(mockMvc, "user2", "password")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}