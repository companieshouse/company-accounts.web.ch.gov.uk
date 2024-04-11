package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.handler.smallfull.financialcommitments.FinancialCommitmentsApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.financialcommitments.FinancialCommitments;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;

@Component
public class FinancialCommitmentsTransformerImpl implements
        NoteTransformer<FinancialCommitments, FinancialCommitmentsApi> {
    @Override
    public FinancialCommitments toWeb(FinancialCommitmentsApi financialCommitmentsApi) {
        FinancialCommitments commitments = new FinancialCommitments();

        if (financialCommitmentsApi == null) {
            return commitments;
        }

        commitments.setFinancialCommitmentsDetails(financialCommitmentsApi.getDetails());
        return commitments;
    }

    @Override
    public FinancialCommitmentsApi toApi(FinancialCommitments financialCommitments) {
        FinancialCommitmentsApi financialCommitmentsApi = new FinancialCommitmentsApi();
        financialCommitmentsApi.setDetails(financialCommitments.getFinancialCommitmentsDetails());
        return financialCommitmentsApi;
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_FINANCIAL_COMMITMENTS;
    }
}
