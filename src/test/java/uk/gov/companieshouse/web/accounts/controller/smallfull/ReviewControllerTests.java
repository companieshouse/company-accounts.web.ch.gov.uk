package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ReviewService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReviewControllerTests {

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String REVIEW_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/review";

    private static final String APPROVAL_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/approval";
    private static final String REVIEW_VIEW = "smallfull/review";

    private static final String REVIEW_MODEL_ATTR = "review";

    @Mock
    CompanyService companyService;

    @Mock
    BalanceSheetService balanceSheetService;

    @Mock
    ReviewService reviewService;

    private MockMvc mockMvc;

    @InjectMocks
    private ReviewController controller;

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get review page success path")
    void getRequestSuccess() throws Exception {

        Review review = new Review();
        review.setBalanceSheet(new BalanceSheet());

        CompanyProfileApi companyProfile = new CompanyProfileApi();
        
        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(balanceSheetService.getBalanceSheetHeadings(companyProfile)).thenReturn(new BalanceSheetHeadings());

        when(reviewService.getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(review);

        this.mockMvc.perform(get(REVIEW_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(REVIEW_VIEW))
                .andExpect(model().attributeExists(REVIEW_MODEL_ATTR));

        verify(companyService, times(1)).getCompanyProfile(COMPANY_NUMBER);

        verify(balanceSheetService, times(1)).getBalanceSheetHeadings(companyProfile);

        verify(reviewService, times(1)).getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Post review page success path")
    void postRequestSuccess() throws Exception {

        this.mockMvc.perform(post(REVIEW_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + APPROVAL_PATH));
    }
}
