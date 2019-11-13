package ru.agilix.auth.test;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.agilix.auth.domain.CheckPhoneNumberRequest;
import ru.agilix.auth.domain.CheckPhoneNumberResponse;
import ru.agilix.auth.domain.SetPinCodeRequest;
import ru.agilix.auth.domain.SetPinCodeResponse;

import static org.junit.Assert.*;

public class AuthenticationStepDefinitions {

    private String url = "http://localhost:8080";
    private RestTemplate restTemplate = new RestTemplate();

    private boolean isResponseOk;
    private String path;

    @Given("I use REST-service on address {string} with version {string}")
    public void iUseRESTServiceOnAddressWithVersion(String url, String version) {
        this.url = url;
    }

    @And("I want to send POST-request {string}")
    public void iSendPOST(String path) {
        this.path = path;
    }

    @When("I sending phoneNumber with value {string}")
    public void iSendingWithValue(String phoneNumber) {
        final CheckPhoneNumberRequest request = new CheckPhoneNumberRequest();
        request.setPhoneNumber(phoneNumber);

        CheckPhoneNumberResponse response = restTemplate.postForObject(url + path, request, CheckPhoneNumberResponse.class);
        this.isResponseOk = (response != null);
    }

    @Then("I receive in JSON body")
    public void iReceiveInJSONBody() {
        assertTrue(isResponseOk);
    }

    @Когда("Устанавливае PIN-кода {string}")
    public void userSetsPinCode(String pinCode) {

    }
    @Тогда("Установка {string} проходит успешно")
    public void isResponseOk(String yesNo) {
        assertEquals(yesNo.equalsIgnoreCase(""), isResponseOk);
    }


    @When("I sending phoneNumber with value {string} and PIN-code {string}")
    public void iSendingPhoneNumberWithValueAndPINCode(String phoneNumber, String pinCode) {
        SetPinCodeRequest request = new SetPinCodeRequest();
        request.setPhoneNumber(phoneNumber);
        request.setPinCode(pinCode);

        try {
            ResponseEntity<SetPinCodeResponse> response = restTemplate.postForEntity(url + path, request, SetPinCodeResponse.class);
            isResponseOk = response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            //isResponseOk = true;
        }
    }
}
