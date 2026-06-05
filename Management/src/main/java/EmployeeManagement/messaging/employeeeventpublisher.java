package EmployeeManagement.messaging;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import java.time.Instant;
import java.util.Map;

@Component
public class employeeeventpublisher {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void publish(Long id, String action) {
        Map<String, Object> event = Map.of(
                "employeeId", id,
                "action", action,
                "timestamp", Instant.now().toString()
        );
        jmsTemplate.convertAndSend("employee.events", event);
        System.out.println("Event Sent: " + event);
    }
}