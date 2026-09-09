package uk.gov.companieshouse.web.accounts.service.company;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UkEstablishmentCompanyNumberService {

    private final List<String> ukEstablishmentCompanyPrefixes;

    public UkEstablishmentCompanyNumberService(@Value("${uk.establishment.company.prefixes}")
        List<String> ukEstablishmentCompanyPrefixes) {
        this.ukEstablishmentCompanyPrefixes = ukEstablishmentCompanyPrefixes;
    }

    public boolean isUkEstablishmentCompany(String companyNumber) {
        String companyNumberUpperCase = companyNumber.toUpperCase();

        return ukEstablishmentCompanyPrefixes.stream()
            .anyMatch(prefix -> companyNumberUpperCase.startsWith(prefix.trim()));
    }
}

