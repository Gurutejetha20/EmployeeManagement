package EmployeeManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import EmployeeManagement.entity.AuditLog;

public interface auditlogrepository extends JpaRepository<AuditLog, Long> {
}