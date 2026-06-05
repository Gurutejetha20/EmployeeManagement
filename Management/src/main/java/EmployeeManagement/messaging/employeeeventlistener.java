package EmployeeManagement.messaging;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

import java.util.Map;
import java.time.LocalDateTime;
import java.util.UUID;

import EmployeeManagement.repository.auditlogrepository;
import EmployeeManagement.entity.AuditLog;

@Component
public class employeeeventlistener {

    @Autowired
    private auditlogrepository repo;

    @JmsListener(destination = "employee.events")
    public void listen(Map<String, Object> event) {
        try {
            AuditLog log = new AuditLog();
            log.setEmployeeId(Long.valueOf(event.get("employeeId").toString()));
            log.setAction(event.get("action").toString());
            log.setSource("JMS");
            log.setTimestamp(LocalDateTime.now());
            log.setMessageId(UUID.randomUUID().toString());
            repo.save(log);
        } catch (Exception e) {
            System.err.println("JMS listener error: " + e.getMessage());
        }
    }
}