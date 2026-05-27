package EmployeeManagement.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import EmployeeManagement.entity.Employee;

public interface employeerepository extends JpaRepository<Employee, Long>{

}
