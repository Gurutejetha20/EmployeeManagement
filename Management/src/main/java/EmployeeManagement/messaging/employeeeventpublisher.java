package EmployeeManagement.messaging;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class employeeeventpublisher {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void publish(Long id, String action, String request, String response) {
        Map<String, Object> event = new HashMap<>();
        event.put("employeeId", id);
        event.put("action", action);
        event.put("timestamp", Instant.now().toString());
        event.put("request", request != null ? request : "");
        event.put("response", response != null ? response : "");

        jmsTemplate.convertAndSend("employee.events", event);
    }
}