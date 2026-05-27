package EmployeeManagement.entity;
import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Override
	public String toString() {
		return "AuditLog [id=" + id + ", employeeId=" + employeeId + ", action=" + action + ", timestamp=" + timestamp
				+ ", source=" + source + ", messageId=" + messageId + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	@Column
    private Long employeeId;
    @Column
    private String action;
    @Column
    private LocalDateTime timestamp;
    @Column
    private String source;
    @Column
    private String messageId;
    public AuditLog() {
    	
    }
    public AuditLog(Long employeeId, String action, LocalDateTime timestamp, String source, String messageId) {
		super();
		this.employeeId = employeeId;
		this.action = action;
		this.timestamp = timestamp;
		this.source = source;
		this.messageId = messageId;
	}

}