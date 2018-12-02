package com.oocl.web.sampleWebApp.domain;

import javax.persistence.*;

@Entity
@Table(name = "parking_boy")
public class ParkingBoy {
    private static Long counter=0L;
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", length = 64, unique = true, nullable = false)
    private String employeeId;

    public Long getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    protected ParkingBoy() {
        this.id=counter++;
    }

    public ParkingBoy(String employeeId) {
        this.employeeId = employeeId;
        this.id=counter++;
    }
}

