package uk.gov.companieshouse.web.accounts.service.cic.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.CicReview;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicStatementsTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CicReviewServiceImplTest {

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Mock
    private CicStatementsService cicStatementsService;

    @Mock
    private CicStatementsTransformer cicStatementsTransformer;

    @Mock
    private CicStatementsApi cicStatementsApi;

    @Mock
    private CicReview cicReview;

    @InjectMocks
    private CicReviewServiceImpl cicReviewService = new CicReviewServiceImpl();

    @Test
    @DisplayName("Get Review - Success Path")
    void getReview() throws ServiceException {

        when(cicStatementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(cicStatementsApi);
        when(cicStatementsTransformer.getCicReview(cicStatementsApi)).thenReturn(cicReview);

        CicReview review = cicReviewService
            .getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(cicReview, review);
    }

    @Test
    @DisplayName("Get Review - ServiceException")
    void getReviewServiceException() throws ServiceException {

        doThrow(ServiceException.class).when(cicStatementsService)
            .getCicStatementsApi(anyString(), anyString());

        assertThrows(ServiceException.class,
            () -> cicReviewService.getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

}
