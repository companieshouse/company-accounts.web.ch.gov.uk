package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.ApprovalApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.model.smallfull.ApprovalDate;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.ApprovalTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApprovalTransformerImplTests {

    private ApprovalTransformer approvalTransformer = new ApprovalTransformerImpl();

    private static String DIRECTOR_NAME = "directorName";

    private static final String APPROVAL_DAY = "7";

    private static final String APPROVAL_MONTH =  "12";

    private static final String APPROVAL_YEAR = "2018";

    @Test
    @DisplayName("Get Approval Api")
    void getApprovalApi() {

        Approval approval = new Approval();
        approval.setDirectorName(DIRECTOR_NAME);

        ApprovalDate approvalDate = new ApprovalDate();
        approvalDate.setDay(APPROVAL_DAY);
        approvalDate.setMonth(APPROVAL_MONTH);
        approvalDate.setYear(APPROVAL_YEAR);

        approval.setApprovalDate(approvalDate);

        ApprovalApi approvalApi = approvalTransformer.getApprovalApi(approval);

        assertEquals(DIRECTOR_NAME, approvalApi.getName());
        assertEquals(Integer.parseInt(APPROVAL_DAY), approvalApi.getDate().getDayOfMonth());
        assertEquals(Integer.parseInt(APPROVAL_MONTH), approvalApi.getDate().getMonthValue());
        assertEquals(Integer.parseInt(APPROVAL_YEAR), approvalApi.getDate().getYear());
    }
}
