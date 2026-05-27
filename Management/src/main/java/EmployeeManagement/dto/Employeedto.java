package EmployeeManagement.dto;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@XmlRootElement(name = "Employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class Employeedto {

    @XmlElement
    @NotBlank
    private String name;

    @XmlElement
    @Email
    @NotBlank
    private String email;

    @XmlElement
    @NotBlank
    private String department;

    @XmlElement
    @NotNull
    @PastOrPresent 
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateOfJoining;



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
}