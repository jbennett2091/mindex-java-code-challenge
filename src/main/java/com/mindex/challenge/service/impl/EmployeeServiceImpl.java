package com.mindex.challenge.service.impl;

import java.util.UUID;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null)
            throw new RuntimeException("Invalid employeeId: " + id);

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure getReportingStructure(String id) {

        final Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null)
            throw new RuntimeException("Invalid employeeId: " + id);

        final ReportingStructure repStr = new ReportingStructure();

        repStr.setEmployee(employee);
        repStr.setNumberOfReports(countReports(employee.getEmployeeId()));

        return repStr;
    }

    protected int countReports(String employeeId) {

        LOG.debug("Count Reports for  [{}]", employeeId);

        final Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null)
            throw new RuntimeException("Invalid employeeId: " + employeeId);

        int count = 0;
        if (employee.getDirectReports() != null)
            for (final Employee theReport : employee.getDirectReports())
            {
                ++count;
                count += countReports(theReport.getEmployeeId());
            }

        return count;
    }
}
