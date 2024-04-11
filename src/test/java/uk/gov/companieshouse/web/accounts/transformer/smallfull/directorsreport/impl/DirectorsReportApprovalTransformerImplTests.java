package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.directorsreport.ApprovalApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportApproval;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;
import uk.gov.companieshouse.web.accounts.transformer.DateTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorsReportApprovalTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorsReportApprovalTransformerImplTests {
    @Mock
    private DateTransformer dateTransformer;

    @InjectMocks
    private DirectorsReportApprovalTransformer approvalTransformer = new DirectorsReportApprovalTransformerImpl();

    private static final String NAME = "name";

    private static final String APPROVAL_DAY = "7";

    private static final String APPROVAL_MONTH =  "12";

    private static final String APPROVAL_YEAR = "2018";

    private static final LocalDate APPROVAL_DATE = LocalDate.of(2018, 12, 7);

    @Test
    @DisplayName("Get Directors Report Approval Api")
    void getDirectorsReportApprovalApi() {
        DirectorsReportApproval approval = new DirectorsReportApproval();
        approval.setName(NAME);

        Date approvalDate = new Date();
        approvalDate.setDay(APPROVAL_DAY);
        approvalDate.setMonth(APPROVAL_MONTH);
        approvalDate.setYear(APPROVAL_YEAR);

        approval.setDate(approvalDate);

        when(dateTransformer.toLocalDate(approvalDate)).thenReturn(APPROVAL_DATE);

        ApprovalApi approvalApi = approvalTransformer.getDirectorsReportApprovalApi(approval);

        assertEquals(NAME, approvalApi.getName());
        assertEquals(Integer.parseInt(APPROVAL_DAY), approvalApi.getDate().getDayOfMonth());
        assertEquals(Integer.parseInt(APPROVAL_MONTH), approvalApi.getDate().getMonthValue());
        assertEquals(Integer.parseInt(APPROVAL_YEAR), approvalApi.getDate().getYear());
    }

    @Test
    @DisplayName("Get Directors Report Approval")
    void getDirectorsReportApproval() {
        ApprovalApi approvalApi = new ApprovalApi();
        approvalApi.setName(NAME);
        approvalApi.setDate(APPROVAL_DATE);

        DirectorsReportApproval approval = approvalTransformer.getDirectorsReportApproval(approvalApi);

        assertEquals(NAME, approval.getName());
        assertNotNull(approval.getDate());
        assertEquals(APPROVAL_DAY, approval.getDate().getDay());
        assertEquals(APPROVAL_MONTH, approval.getDate().getMonth());
        assertEquals(APPROVAL_YEAR, approval.getDate().getYear());
    }
}
