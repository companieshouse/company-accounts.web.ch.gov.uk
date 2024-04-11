package uk.gov.companieshouse.web.accounts.transformer.smallfull.relatedpartytransactions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RptTransactionApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RptTransactionLinks;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransaction;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionToAdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RptTransactionsTransformerImplTest {
    @Mock
    private RptBreakdownTransformer rptBreakdownTransformer;

    @Mock
    private RptTransactionToAdd rptTransactionToAdd;

    @InjectMocks
    private RptTransactionsTransformer rptTransactionsTransformer = new RptTransactionsTransformerImpl();

    private static final String NAME_OF_RELATED_PARTY = "nameOfRelatedParty";

    private static final String DESCRIPTION_OF_TRANSACTION = "description";

    private static final String TRANSACTION_TYPE = "transactionType";

    private static final String RELATIONSHIP = "relationship";

    private static final String RPT_TRANSACTION_ID = "rptTransactionId";

    private static final String RPT_TRANSACTIONS_SELF_LINK =
            "/transactions/transactionId/company-accounts/companyAccountsId/small-full/notes/related-party-transactions/transactions/" + RPT_TRANSACTION_ID;

    @Test
    @DisplayName("Get RPT transaction API - no breakdown")
    void getRptTransactionNoBreakdown() {
        RptTransactionToAdd rptTransactionToAdd = new RptTransactionToAdd();
        rptTransactionToAdd.setNameOfRelatedParty(NAME_OF_RELATED_PARTY);
        rptTransactionToAdd.setDescriptionOfTransaction(DESCRIPTION_OF_TRANSACTION);
        rptTransactionToAdd.setRelationship(RELATIONSHIP);
        rptTransactionToAdd.setTransactionType(TRANSACTION_TYPE);

        RptTransactionApi rptTransactionApi = rptTransactionsTransformer.getRptTransactionsApi(rptTransactionToAdd);

        assertNotNull(rptTransactionApi);
        assertEquals(NAME_OF_RELATED_PARTY, rptTransactionApi.getNameOfRelatedParty());
        assertEquals(DESCRIPTION_OF_TRANSACTION, rptTransactionApi.getDescriptionOfTransaction());
        assertEquals(TRANSACTION_TYPE, rptTransactionApi.getTransactionType());
        assertEquals(DESCRIPTION_OF_TRANSACTION, rptTransactionApi.getDescriptionOfTransaction());
        assertNull(rptTransactionApi.getBreakdown());
    }

    @Test
    @DisplayName("Get all RPT transactions")
    void getAllRptTransactions() {
        RptTransactionApi rptTransactionApi = new RptTransactionApi();
        rptTransactionApi.setNameOfRelatedParty(NAME_OF_RELATED_PARTY);
        rptTransactionApi.setDescriptionOfTransaction(DESCRIPTION_OF_TRANSACTION);
        rptTransactionApi.setRelationship(RELATIONSHIP);
        rptTransactionApi.setTransactionType(TRANSACTION_TYPE);
        rptTransactionApi.setBreakdown(rptBreakdownTransformer.mapRptTransactionsBreakdownToApi(rptTransactionToAdd));

        RptTransactionLinks rptTransactionLinks = new RptTransactionLinks();
        rptTransactionLinks.setSelf(RPT_TRANSACTIONS_SELF_LINK);

        rptTransactionApi.setLinks(rptTransactionLinks);

        RptTransaction[] allRptTransactions = rptTransactionsTransformer.getAllRptTransactions(new RptTransactionApi[]{rptTransactionApi});

        assertEquals(1, allRptTransactions.length);
        assertEquals(NAME_OF_RELATED_PARTY, allRptTransactions[0].getNameOfRelatedParty());
        assertEquals(DESCRIPTION_OF_TRANSACTION, allRptTransactions[0].getDescriptionOfTransaction());
        assertEquals(TRANSACTION_TYPE, allRptTransactions[0].getTransactionType());
        assertEquals(RELATIONSHIP, allRptTransactions[0].getRelationship());
        assertEquals(rptTransactionToAdd.getBreakdown(), allRptTransactions[0].getBreakdown());
        assertEquals(RPT_TRANSACTION_ID, allRptTransactions[0].getId());
    }
}