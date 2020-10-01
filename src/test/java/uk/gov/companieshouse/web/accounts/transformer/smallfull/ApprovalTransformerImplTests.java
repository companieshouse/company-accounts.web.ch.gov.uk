package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.ApprovalApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;
import uk.gov.companieshouse.web.accounts.transformer.DateTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.ApprovalTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApprovalTransformerImplTests {

    @Mock
    private DateTransformer dateTransformer;

    @InjectMocks
    private ApprovalTransformer approvalTransformer = new ApprovalTransformerImpl();

    private static final String DIRECTOR_NAME = "directorName";

    private static final String APPROVAL_DAY = "7";

    private static final String APPROVAL_MONTH =  "12";

    private static final String APPROVAL_YEAR = "2018";

    private static final LocalDate APPROVAL_DATE = LocalDate.of(2018, 12, 7);

    @Test
    @DisplayName("Get Approval Api")
    void getApprovalApi() {

        Approval approval = new Approval();
        approval.setDirectorName(DIRECTOR_NAME);

        Date approvalDate = new Date();
        approvalDate.setDay(APPROVAL_DAY);
        approvalDate.setMonth(APPROVAL_MONTH);
        approvalDate.setYear(APPROVAL_YEAR);

        approval.setDate(approvalDate);

        when(dateTransformer.toLocalDate(approvalDate)).thenReturn(APPROVAL_DATE);

        ApprovalApi approvalApi = approvalTransformer.getApprovalApi(approval);

        assertEquals(DIRECTOR_NAME, approvalApi.getName());
        assertEquals(Integer.parseInt(APPROVAL_DAY), approvalApi.getDate().getDayOfMonth());
        assertEquals(Integer.parseInt(APPROVAL_MONTH), approvalApi.getDate().getMonthValue());
        assertEquals(Integer.parseInt(APPROVAL_YEAR), approvalApi.getDate().getYear());
    }
}
