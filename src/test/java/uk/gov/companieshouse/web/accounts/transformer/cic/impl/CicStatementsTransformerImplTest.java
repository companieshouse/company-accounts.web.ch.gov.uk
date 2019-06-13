package uk.gov.companieshouse.web.accounts.transformer.cic.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.api.model.accounts.cic.statements.ReportStatementsApi;
import uk.gov.companieshouse.web.accounts.model.cic.CicReview;
import uk.gov.companieshouse.web.accounts.model.cic.statements.CompanyActivitiesAndImpact;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicStatementsTransformer;

public class CicStatementsTransformerImplTest {

    private CicStatementsTransformer transformer = new CicStatementsTransformerImpl();

    private static final String COMPANY_ACTIVITIES_AND_IMPACT = "companyActivitiesAndImpact";
    private static final String UPDATED_COMPANY_ACTIVITIES_AND_IMPACT = "updatedCompanyActivitiesAndImpact";

    private static final String CONSULTATION_WITH_STAKEHOLDERS = "consultationWithStakeholders";

    private static final String DIRECTORS_REMUNERATION = "directorsRemuneration";

    private static final String TRANSFER_OF_ASSETS = "transferOfAssets";

    @Test
    @DisplayName("Get company activities and impact - null statements object")
    void getCompanyActivitiesAndImpactForNullStatementsObject() {

        CompanyActivitiesAndImpact companyActivitiesAndImpact =
                transformer.getCompanyActivitiesAndImpact(null);

        assertNotNull(companyActivitiesAndImpact);
        assertNull(companyActivitiesAndImpact.getActivitiesAndImpact());
    }

    @Test
    @DisplayName("Get company activities and impact")
    void getCompanyActivitiesAndImpact() {

        CompanyActivitiesAndImpact companyActivitiesAndImpact =
                transformer.getCompanyActivitiesAndImpact(createCicStatementsApi());

        assertNotNull(companyActivitiesAndImpact);
        assertEquals(COMPANY_ACTIVITIES_AND_IMPACT, companyActivitiesAndImpact.getActivitiesAndImpact());
    }

    @Test
    @DisplayName("Set company activities and impact")
    void setCompanyActivitiesAndImpact() {

        CompanyActivitiesAndImpact companyActivitiesAndImpact = new CompanyActivitiesAndImpact();
        companyActivitiesAndImpact.setActivitiesAndImpact(UPDATED_COMPANY_ACTIVITIES_AND_IMPACT);

        CicStatementsApi cicStatementsApi = createCicStatementsApi();

        transformer.setCompanyActivitiesAndImpact(companyActivitiesAndImpact, cicStatementsApi);

        ReportStatementsApi reportStatements = cicStatementsApi.getReportStatements();
        assertEquals(UPDATED_COMPANY_ACTIVITIES_AND_IMPACT, reportStatements.getCompanyActivitiesAndImpact());
        assertEquals(CONSULTATION_WITH_STAKEHOLDERS, reportStatements.getConsultationWithStakeholders());
        assertEquals(DIRECTORS_REMUNERATION, reportStatements.getDirectorsRemuneration());
        assertEquals(TRANSFER_OF_ASSETS, reportStatements.getTransferOfAssets());
    }

    @Test
    @DisplayName("Get cicReview")
    void getCicReview() {

        CicReview cicReview = transformer.getCicReview(createCicStatementsApi());

        assertNotNull(cicReview);
        assertEquals(COMPANY_ACTIVITIES_AND_IMPACT, cicReview.getActivitiesAndImpact());
        assertEquals(CONSULTATION_WITH_STAKEHOLDERS, cicReview.getConsultationWithStakeholders());
        assertEquals(DIRECTORS_REMUNERATION, cicReview.getDirectorsRemuneration());
        assertEquals(TRANSFER_OF_ASSETS, cicReview.getTransferOfAssets());
    }

    CicStatementsApi createCicStatementsApi() {

        ReportStatementsApi reportStatementsApi = new ReportStatementsApi();
        reportStatementsApi.setCompanyActivitiesAndImpact(COMPANY_ACTIVITIES_AND_IMPACT);
        reportStatementsApi.setConsultationWithStakeholders(CONSULTATION_WITH_STAKEHOLDERS);
        reportStatementsApi.setDirectorsRemuneration(DIRECTORS_REMUNERATION);
        reportStatementsApi.setTransferOfAssets(TRANSFER_OF_ASSETS);

        CicStatementsApi cicStatementsApi = new CicStatementsApi();
        cicStatementsApi.setReportStatements(reportStatementsApi);

        return cicStatementsApi;
    }
}
