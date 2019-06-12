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
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.company.CompanyDetail;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GovukCompanyDetailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CompanyService companyService;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private GovukCompanyDetailController controller;

    @Mock
    private CompanyDetail companyDetail;

    private static final String COMPANY_NUMBER = "number";
    private static final String COMPANY_DETAIL_PATH = "/accounts/company/" + COMPANY_NUMBER + "/details";
    private static final String COMPANY_DETAIL_VIEW = "company/companyDetail";
    private static final String ERROR_VIEW = "error";
    private static final String COMPANY_DETAIL_MODEL_ATTR = "companyDetail";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String TEMPLATE_HEADING_MODEL_ATTR = "templateHeading";
    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";
    private static final String TEMPLATE_SHOW_CONTINUE_MODEL_ATTR = "showContinue";

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
    @DisplayName("Post Gov uk Company Details")
    void postRequest() throws Exception {

        when(navigatorService.getNextControllerRedirect(controller.getClass(), COMPANY_NUMBER))
                .thenReturn(MOCK_CONTROLLER_PATH);

        mockMvc.perform(post(COMPANY_DETAIL_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }
}
