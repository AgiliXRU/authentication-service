package ru.agilix.message.sms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import ru.agilix.message.sms.domain.SmsDTO;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${SMS_GATE_URL}")
    private String smsGateUrl;

    @Autowired
    private RestOperations restOperations;

    @PostConstruct
    private void postConstruct() {
        System.out.println("SMS GW: " + smsGateUrl);
    }

    @Override
    public Optional<String> send(SmsDTO smsDTO) {
        ResponseEntity<String> response = restOperations.postForEntity(smsGateUrl + "/v1/sms/send", smsDTO, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        } else {
            System.err.println("Failed to send SMS to " + smsDTO.getNumber());
            return Optional.empty();
        }

    }

}
