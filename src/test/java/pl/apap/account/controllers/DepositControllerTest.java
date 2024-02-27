package pl.apap.account.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.apap.account.model.User;
import pl.apap.account.services.ConfirmationEmailService;
import pl.apap.account.services.DepositService;
import java.math.BigDecimal;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DepositControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private DepositService depositService;

    @MockBean
    private ConfirmationEmailService emailService;

    @InjectMocks
    private DepositController depositController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testDeposit() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        User testUser = new User();
        testUser.setName("John");
        testUser.setAccountBalance(BigDecimal.valueOf(100));
        session.setAttribute("user", testUser);

        doAnswer(invocation -> {
            User modifiedUser = invocation.getArgument(0);
            BigDecimal amount = invocation.getArgument(1);
            modifiedUser.setAccountBalance(modifiedUser.getAccountBalance().add(amount));
            return null;
        }).when(depositService).deposit(any(User.class), any(BigDecimal.class));

        // When
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/deposit").session(session).param("amount", "50")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/deposit/success")).andReturn();

        // Then
        Mockito.verify(depositService).deposit(eq(testUser), eq(BigDecimal.valueOf(50)));

        assertThat(result.getFlashMap().get("userName")).isEqualTo("John");

        assertThat(result.getFlashMap().get("accBalance")).isEqualTo(BigDecimal.valueOf(150));
        assertThat(result.getFlashMap().get("amount")).isEqualTo(BigDecimal.valueOf(50));

        assertThat(testUser.getAccountBalance()).isEqualByComparingTo(BigDecimal.valueOf(150));
    }


    @Test
    @WithMockUser
    public void testShowDepositForm() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        User user = new User();
        user.setName("John");
        user.setAccountBalance(BigDecimal.valueOf(100));
        user.setInvestedMoney(BigDecimal.valueOf(300));
        user.setTotalSpent(BigDecimal.valueOf(100));
        user.setTotalEarned(BigDecimal.valueOf(200));

        session.setAttribute("user", user);

        // When
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/deposit").session(session)).andExpect(status().isOk()).andExpect(view().name("deposit")).andExpect(model().attribute("userName", "John")).andExpect(model().attribute("accBalance", BigDecimal.valueOf(100))).andExpect(model().attribute("investedMoney", BigDecimal.valueOf(300))).andExpect(model().attribute("totalSpent", BigDecimal.valueOf(100))).andExpect(model().attribute("totalEarned", BigDecimal.valueOf(200))).andExpect(model().attribute("amount", BigDecimal.ZERO)).andReturn();
    }

    @Test
    @WithAnonymousUser
    public void testShowDepositFormRedirectWhenNoUserInSession() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();

        // When/Then
        mvc.perform(MockMvcRequestBuilders.get("/deposit").session(session)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));
    }


    @Test
    @WithMockUser
    public void testDepositWithError() throws Exception {
        // Given
        User user = new User();
        user.setName("John");
        user.setAccountBalance(BigDecimal.valueOf(100));

        doThrow(new RuntimeException("Deposit failed")).when(depositService).deposit(eq(user), any(BigDecimal.class));

        // When/Then
        mvc.perform(post("/deposit").sessionAttr("user", user).param("amount", "0")).andExpect(status().isOk()).andExpect(view().name("deposit")).andExpect(model().attribute("userName", "John")).andExpect(model().attribute("accBalance", BigDecimal.valueOf(100))).andExpect(model().attribute("amount", BigDecimal.ZERO)).andExpect(model().attribute("error", "Amount must be greater than zero for deposit."));
    }


    @Test
    public void testDepositSuccessEmail() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setAccountBalance(BigDecimal.valueOf(150));
        session.setAttribute("user", user);

        // When
        mvc.perform(post("/deposit/success").session(session)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/site")).andReturn();

        // Then
        Mockito.verify(emailService).sendConfirmationEmail(eq("john@example.com"), eq("Deposit Confirmation"), contains("John"));
    }

}
