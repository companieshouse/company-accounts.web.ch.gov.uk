package uk.gov.companieshouse.web.accounts.controller.govuk;

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
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.company.CompanyDetail;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GovukCompanyDetailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private GovukCompanyDetailController controller;

    @Mock
    private CompanyDetail companyDetail;

    @Mock
    private CompanyProfileApi companyProfile;

    private static final String COMPANY_NUMBER = "number";
    private static final String COMPANY_DETAIL_PATH = "/accounts/company/" + COMPANY_NUMBER + "/details";
    private static final String COMPANY_DETAIL_VIEW = "company/companyDetail";
    private static final String ERROR_VIEW = "error";
    private static final String COMPANY_DETAIL_MODEL_ATTR = "companyDetail";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String TEMPLATE_HEADING_MODEL_ATTR = "templateHeading";
    private static final String TEMPLATE_SHOW_CONTINUE_MODEL_ATTR = "showContinue";

    private static final String SMALL_FULL_STEPS_TO_COMPLETE_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/company/" + COMPANY_NUMBER + "/small-full/steps-to-complete";

    private static final String CIC_STEPS_TO_COMPLETE_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/company/" + COMPANY_NUMBER + "/cic/steps-to-complete";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get Gov uk Company Details - Success")
    void getRequestSuccess() throws Exception {

        when(companyService.getCompanyDetail(COMPANY_NUMBER)).thenReturn(companyDetail);

        mockMvc.perform(get(COMPANY_DETAIL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(COMPANY_DETAIL_VIEW))
                .andExpect(model().attributeExists(COMPANY_DETAIL_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_HEADING_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_SHOW_CONTINUE_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get Gov uk Company Details - Throws Exception")
    void getRequestThrowsException() throws Exception {

        when(companyService.getCompanyDetail(COMPANY_NUMBER)).thenThrow(ServiceException.class);

        mockMvc.perform(get(COMPANY_DETAIL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post Gov uk Company Details - CIC Company")
    void postRequestCicCompany() throws Exception {

        when(companyService.getCompanyProfile(COMPANY_NUMBER))
                .thenReturn(companyProfile);
        when(companyProfile.isCommunityInterestCompany()).thenReturn(true);

        mockMvc.perform(post(COMPANY_DETAIL_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(CIC_STEPS_TO_COMPLETE_PATH));
    }

    @Test
    @DisplayName("Post Gov uk Company Details - Non-CIC Company")
    void postRequestNonCicCompany() throws Exception {

        when(companyService.getCompanyProfile(COMPANY_NUMBER))
                .thenReturn(companyProfile);
        when(companyProfile.isCommunityInterestCompany()).thenReturn(false);

        mockMvc.perform(post(COMPANY_DETAIL_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(SMALL_FULL_STEPS_TO_COMPLETE_PATH));
    }

    @Test
    @DisplayName("Post Gov uk Company Details - Throws Exception")
    void postRequestThrowsException() throws Exception {

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenThrow(ServiceException.class);

        mockMvc.perform(post(COMPANY_DETAIL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }
}
