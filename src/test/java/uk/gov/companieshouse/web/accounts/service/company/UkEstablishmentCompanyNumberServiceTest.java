package uk.gov.companieshouse.web.accounts.service.company;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class UkEstablishmentCompanyNumberServiceTest {

    private final UkEstablishmentCompanyNumberService service =
        new UkEstablishmentCompanyNumberService(List.of("BR", " OE "));

    @Test
    void isUkEstablishmentCompanyReturnsTrueForConfiguredPrefix() {
        assertTrue(service.isUkEstablishmentCompany("br123456"));
    }

    @Test
    void isUkEstablishmentCompanyReturnsTrueForTrimmedPrefix() {
        assertTrue(service.isUkEstablishmentCompany("OE654321"));
    }

    @Test
    void isUkEstablishmentCompanyReturnsFalseForNonUkEstablishmentPrefix() {
        assertFalse(service.isUkEstablishmentCompany("SC123456"));
    }
}

