package ru.agilix.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/v1/sms/")
public class SmsController {

    @Value("${TWILIO_AUTH_SID}")
    private String twilioAuthSid;
    @Value("${TWILIO_AUTH_TOKEN}")
    private String twilioAuthToken;
    @Value("${TWILIO_FROM}")
    private String twilioFrom;

    @PostConstruct
    public void postConstruct() {
        Twilio.init(twilioAuthSid, twilioAuthToken);
    }

    @PostMapping("send")
    public ResponseEntity<String> sendSMS(@RequestBody SmsSendRequest requestBody) {
        System.out.println(requestBody.getNumber() + " " + requestBody.getBody());
        Message message = Message.creator(
                new PhoneNumber(requestBody.getNumber()),
                new PhoneNumber(twilioFrom),
                requestBody.getBody()).create();
        return ResponseEntity.ok(message.getSid());
    }

}
