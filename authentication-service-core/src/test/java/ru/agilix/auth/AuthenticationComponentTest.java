package ru.agilix.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestOperations;
import ru.agilix.auth.service.GeneratorService;
import ru.agilix.dao.OneTimePasswordRepository;
import ru.agilix.dao.TokenRepository;
import ru.agilix.domain.OneTimePassword;
import ru.agilix.domain.Token;
import ru.agilix.message.sms.domain.SmsDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class AuthenticationComponentTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;

    @MockBean
    private GeneratorService generatorService;

    static {
        System.setProperty("SMS_GATE_URL", "someUrl");
    }

    @MockBean
    private RestOperations restOperations;


    @BeforeEach
    void setUp() {
        tokenRepository.deleteAll();
        oneTimePasswordRepository.deleteAll();
    }

    @Test
    void userSetsPinCode() throws Exception {
        String phoneNumber = "+79991234567";
        String pinCode = "1111";
        String smsCode = "123456";
        String confirmationToken = "2222-3333";

        //Check phone
        performPostRequest(
                "/v1/auth/checkPhoneNumber", "{\"phoneNumber\":\"" + phoneNumber + "\"}",
                200, "{\"available\": false\n}");

        //Verify PinCode, expects 403 (PinCode is not set)
        performPostRequest(
                "/v1/auth/verifyPinCode", "{\"phoneNumber\":\"" + phoneNumber + "\", \"pinCode\":\"" + pinCode + "\"}",
                403, "{\"errorMessage\": \"PinCode is not valid\"}");

        //Set PinCode and send SmsCode
        when(generatorService.generateOneTimePassword()).thenReturn(smsCode);
        when(restOperations.postForEntity(nullable(String.class), any(SmsDTO.class), ArgumentMatchers.<Class<String>>any())).thenReturn(ResponseEntity.ok(""));
        when(generatorService.generateToken()).thenReturn(confirmationToken);
        performPostRequest(
                "/v1/auth/setPinCode", "{\"phoneNumber\":\"" + phoneNumber + "\", \"pinCode\":\"" + pinCode + "\"}",
                200, "{\"confirmationToken\": \"" + confirmationToken + "\"}"
        );

        //Verify SmsCode in DB
        OneTimePassword oneTimePassword = oneTimePasswordRepository.findAll().iterator().next();
        assertNotNull(oneTimePassword.getMsgId());
        assertTrue(LocalDateTime.now().isAfter(oneTimePassword.getCreatedDateTime()));

        //Verify PinCode, expects 403 (PinCode is not approve)
        performPostRequest(
                "/v1/auth/verifyPinCode", "{\"phoneNumber\":\"" + phoneNumber + "\", \"pinCode\":\"" + pinCode + "\"}",
                403, "{\"errorMessage\": \"PinCode is not valid\"}");

        //Verify SmsCode, expects 403 (SmsCode is wrong)
        performPostRequest(
                "/v1/auth/verifySmsCode", "{\"smsCode\": \"" + smsCode.concat("0") + "\" , \"confirmationToken\": \"" + confirmationToken + "\"}",
                403, "{}"
        );

        //Verify SmsCode
        performPostRequest(
                "/v1/auth/verifySmsCode", "{\"smsCode\": \"" + smsCode + "\" , \"confirmationToken\": \"" + confirmationToken + "\"}",
                200, "{}"
        );

        //Verify PinCode
        performPostRequest(
                "/v1/auth/verifyPinCode", "{\"phoneNumber\":\"" + phoneNumber + "\", \"pinCode\":\"" + pinCode + "\"}",
                200, "{\"confirmationToken\": \"" + confirmationToken + "\"}");

        //Verify token in DB
        Token token = tokenRepository.findAll().iterator().next();
        assertTrue(LocalDateTime.now().isAfter(token.getCreatedDateTime()));

        //Check phone
        performPostRequest(
                "/v1/auth/checkPhoneNumber", "{\"phoneNumber\":\"" + phoneNumber + "\"}",
                200, "{\"available\": true\n}");

    }

    private void performPostRequest(String url, String requestBody, Integer statusCode, String responseBody) throws Exception {
        ResultActions resultActions = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(requestBody));
        resultActions.andExpect(status().is(statusCode)).andExpect(content().json(responseBody));
    }


}
