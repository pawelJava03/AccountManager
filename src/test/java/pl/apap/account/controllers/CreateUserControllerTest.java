package pl.apap.account.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.apap.account.model.User;
import pl.apap.account.repositories.UsersRepository;
import pl.apap.account.services.EmailSenderService;
import pl.apap.account.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CreateUserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private EmailSenderService emailSenderService;

    @Autowired
    private MockMvc mvc;


    @Test
    public void testShowCreateUserForm() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("create_user"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testCreateUserSuccess() throws Exception {
        // Given
        User user = new User();
        user.setEmail("test@example.com");

        when(usersRepository.existsByEmail("test@example.com")).thenReturn(false);
        doNothing().when(userService).createUser(any(User.class));

        // When
        mvc.perform(MockMvcRequestBuilders.post("/user/create")
                        .flashAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create_user_successfully"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.is(user)))
                .andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.nullValue()));
    }





}