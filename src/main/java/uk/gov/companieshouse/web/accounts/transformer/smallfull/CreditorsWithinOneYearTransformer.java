package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.CreditorsWithinOneYearApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;

public interface CreditorsWithinOneYearTransformer {

	CreditorsWithinOneYear getCreditorsWithinOneYear(CreditorsWithinOneYearApi creditorsWithinOneYearApi);

	CreditorsWithinOneYearApi setCreditorsWithinOneYear(CreditorsWithinOneYear creditorsWithinOneYear);
}
