package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
    }

    @Test
    public void testCreateReadUpdate() {
        final Compensation bean = new Compensation();
        bean.setEmployeeId("1234");
        bean.setSalary(BigDecimal.valueOf(12.34d));
        bean.setEffectiveDate(Instant.now());

        // Create checks
        final Compensation createdBean = restTemplate.postForEntity(compensationUrl, bean, Compensation.class)
                .getBody();

        assertBeanEquivalence(bean, createdBean);


        // No Read checks - compensationId is not a thing

        // No Update checks - ditto
    }

    private static void assertBeanEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getSalary().doubleValue(), actual.getSalary().doubleValue(), .001);
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }

    @Test
    public void testgetCompensation() {

        final String employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        final List<Compensation> list = compensationService.getCompensation(employeeId);
        assertNotNull(list);

        assertEquals(3, list.size());
    }

}
