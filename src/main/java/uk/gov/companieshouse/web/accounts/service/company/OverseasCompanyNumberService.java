package uk.gov.companieshouse.web.accounts.service.company;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OverseasCompanyNumberService {

    private final List<String> overseasCompanyPrefixes;

    public OverseasCompanyNumberService(@Value("${overseas.company.prefixes}")
        List<String> overseasCompanyPrefixes) {
        this.overseasCompanyPrefixes = overseasCompanyPrefixes;
    }

    public boolean isOverseasCompany(String companyNumber) {
        String companyNumberUpperCase = companyNumber.toUpperCase();

        return overseasCompanyPrefixes.stream()
            .anyMatch(prefix -> companyNumberUpperCase.startsWith(prefix.trim()));
    }
}
