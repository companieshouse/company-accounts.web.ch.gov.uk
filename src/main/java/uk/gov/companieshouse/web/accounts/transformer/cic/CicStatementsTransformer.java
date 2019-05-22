package uk.gov.companieshouse.web.accounts.transformer.cic;

import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.model.cic.CicReview;
import uk.gov.companieshouse.web.accounts.model.cic.statements.CompanyActivitiesAndImpact;
import uk.gov.companieshouse.web.accounts.model.cic.statements.ConsultationWithStakeholders;
import uk.gov.companieshouse.web.accounts.model.cic.statements.DirectorsRemuneration;
import uk.gov.companieshouse.web.accounts.model.cic.statements.TransferOfAssets;

public interface CicStatementsTransformer {

    /**
     * Gets the company's activities and impact from a given cic statements API resource
     *
     * @param cicStatementsApi The cic statements API resource
     * @return the company's activities and impact, or an unpopulated model if the API resource is
     * null
     */
    CompanyActivitiesAndImpact getCompanyActivitiesAndImpact(CicStatementsApi cicStatementsApi);

    /**
     * Sets the company's activities and impact on the cic statements API resource
     *
     * @param companyActivitiesAndImpact The company's activities and impact
     * @param cicStatementsApi The cic statements API resource
     */
    void setCompanyActivitiesAndImpact(
        CompanyActivitiesAndImpact companyActivitiesAndImpact, CicStatementsApi cicStatementsApi);


    ConsultationWithStakeholders getConsultationWithStakeholders(CicStatementsApi cicStatementsApi);

    void setConsultationWithStakeholders(
        ConsultationWithStakeholders consultationWithStakeholders,
        CicStatementsApi cicStatementsApi);

    DirectorsRemuneration getDirectorsRemuneration(CicStatementsApi cicStatementsApi);

    void setDirectorsRemuneration(
        DirectorsRemuneration directorsRemuneration,
        CicStatementsApi cicStatementsApi);

    TransferOfAssets getTransferOfAssets(CicStatementsApi cicStatementsApi);

    void setTransferOfAssets(TransferOfAssets transferOfAssets, CicStatementsApi cicStatementsApi);

    CicReview getCicReview(CicStatementsApi cicStatementsApi);
}
