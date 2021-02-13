package com.mindex.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStream;

@Component
public class DataBootstrap {
    private static final String DATASTORE_LOCATION_EMPLOYEE = "/static/employee_database.json";
    private static final String DATASTORE_LOCATION_COMPENSATION = "/static/compensation_database.json";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        initEmployees();
        initCompensation();
    }

    protected void initEmployees() {
        InputStream inputStream = this.getClass().getResourceAsStream(DATASTORE_LOCATION_EMPLOYEE);

        Employee[] employees = null;

        try {
            employees = objectMapper.readValue(inputStream, Employee[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Employee employee : employees)
            employeeRepository.insert(employee);
    }

    protected void initCompensation() {
        InputStream inputStream = this.getClass().getResourceAsStream(DATASTORE_LOCATION_COMPENSATION);

        Compensation[] comps = null;

        try {
            comps = objectMapper.readValue(inputStream, Compensation[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Compensation comp : comps)
            compensationRepository.insert(comp);
    }
}
