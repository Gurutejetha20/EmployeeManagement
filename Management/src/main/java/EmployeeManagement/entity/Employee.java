package EmployeeManagement.entity;
import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "employees")
public class Employee {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getDepartment() {
			return department;
		}
		public void setDepartment(String department) {
			this.department = department;
		}
		public LocalDate getDateOfJoining() {
			return dateOfJoining;
		}
		public void setDateOfJoining(LocalDate dateOfJoining) {
			this.dateOfJoining = dateOfJoining;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}
		@Column
		private String name;
		@Column(unique = true)
		private String email;
		@Column
		private String department;
		@Column
		private LocalDate dateOfJoining;
		@Column
		private String status = "ACTIVE";
		@Column
		private LocalDateTime createdAt = LocalDateTime.now();
		@Column
		private LocalDateTime updatedAt;
		@Override
		public String toString() {
			return "Employee [id=" + id + ", name=" + name + ", email=" + email + ", department=" + department
					+ ", dateOfJoining=" + dateOfJoining + ", status=" + status + ", createdAt=" + createdAt
					+ ", updatedAt=" + updatedAt + "]";
		}
		public Employee()
		{
			
		}
		public Employee(String name, String email, String department, LocalDate dateOfJoining, String status,
				LocalDateTime createdAt, LocalDateTime updatedAt) {
			super();
			this.name = name;
			this.email = email;
			this.department = department;
			this.dateOfJoining = dateOfJoining;
			this.status = status;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
		}
		
	}

