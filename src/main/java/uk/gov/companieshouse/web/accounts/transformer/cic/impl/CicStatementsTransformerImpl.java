package uk.gov.companieshouse.web.accounts.transformer.cic.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.model.cic.statements.CompanyActivitiesAndImpact;
import uk.gov.companieshouse.web.accounts.model.cic.statements.DirectorsRemuneration;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicStatementsTransformer;

@Component
public class CicStatementsTransformerImpl implements CicStatementsTransformer {

    /**
     * {@inheritDoc}
     */
    @Override
    public CompanyActivitiesAndImpact getCompanyActivitiesAndImpact(CicStatementsApi cicStatementsApi) {

        if (cicStatementsApi == null) {
            return new CompanyActivitiesAndImpact();
        }

        CompanyActivitiesAndImpact companyActivitiesAndImpact = new CompanyActivitiesAndImpact();
        companyActivitiesAndImpact.setActivitiesAndImpact(
                cicStatementsApi.getReportStatements().getCompanyActivitiesAndImpact());

        return companyActivitiesAndImpact;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCompanyActivitiesAndImpact(CompanyActivitiesAndImpact companyActivitiesAndImpact,
            CicStatementsApi cicStatementsApi) {

        cicStatementsApi.getReportStatements().setCompanyActivitiesAndImpact(
                companyActivitiesAndImpact.getActivitiesAndImpact());
    }

    @Override
    public DirectorsRemuneration getDirectorsRemuneration(CicStatementsApi cicStatementsApi) {
        DirectorsRemuneration directorsRemuneration = new DirectorsRemuneration();
        directorsRemuneration.setDirectorsRemuneration(
            cicStatementsApi.getReportStatements().getDirectorsRemuneration());

        return directorsRemuneration;
    }

    @Override
    public void setDirectorsRemuneration(DirectorsRemuneration directorsRemuneration,
        CicStatementsApi cicStatementsApi) {
        cicStatementsApi.getReportStatements().setDirectorsRemuneration(
            directorsRemuneration.getDirectorsRemuneration());
    }

}
