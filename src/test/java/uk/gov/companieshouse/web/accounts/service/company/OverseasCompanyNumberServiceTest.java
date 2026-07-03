package uk.gov.companieshouse.web.accounts.service.company;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class OverseasCompanyNumberServiceTest {

    private final OverseasCompanyNumberService service =
        new OverseasCompanyNumberService(List.of("FC", "NF", " SF "));

    @Test
    void isOverseasCompanyReturnsTrueForConfiguredPrefix() {
        assertTrue(service.isOverseasCompany("fc123456"));
    }

    @Test
    void isOverseasCompanyReturnsTrueForTrimmedPrefix() {
        assertTrue(service.isOverseasCompany("SF654321"));
    }

    @Test
    void isOverseasCompanyReturnsFalseForNonOverseasPrefix() {
        assertFalse(service.isOverseasCompany("SC123456"));
    }
}

