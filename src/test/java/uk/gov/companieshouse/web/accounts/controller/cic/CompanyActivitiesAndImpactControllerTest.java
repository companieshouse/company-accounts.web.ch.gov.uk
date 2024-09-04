package uk.gov.companieshouse.web.accounts.controller.cic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.CompanyActivitiesAndImpact;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CompanyActivitiesAndImpactService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompanyActivitiesAndImpactControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CompanyActivitiesAndImpactService companyActivitiesAndImpactService;

    @Mock
    NavigatorService navigatorService;

    @Mock
    private MockHttpServletRequest mockHttpServletRequest;

    @Mock
    private MockHttpSession mockHttpSession;

    @InjectMocks
    private CompanyActivitiesAndImpactController companyActivitiesAndImpactController;

    private static final String COMPANY_ACTIVITY_PATH = "/company/012/transaction/345/company-accounts/678/cic/company-activity";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String COMPANY_ACTIVITY_MODEL_ATTR = "companyActivitiesAndImpact";

    private static final String COMPANY_ACTIVITY_VIEW = "cic/companyActivitiesAndImpact";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    @BeforeEach
    public void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(companyActivitiesAndImpactController)
                .build();
    }

    @Test
    @DisplayName("Get CompanyActivitiesAndImpact view - success path")
    void getRequestCompanyActivitiesAndImpactSuccess() throws Exception {

        when(companyActivitiesAndImpactService
                .getCompanyActivitiesAndImpact(anyString(), anyString()))
                .thenReturn(new CompanyActivitiesAndImpact());

        this.mockMvc.perform(get(COMPANY_ACTIVITY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(COMPANY_ACTIVITY_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_ACTIVITY_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get CompanyActivitiesAndImpact view - service exception")
    void getRequestCompanyActivitiesAndImpactServiceException() throws Exception {

        when(companyActivitiesAndImpactService
                .getCompanyActivitiesAndImpact(anyString(), anyString()))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(COMPANY_ACTIVITY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Accept CompanyActivitiesAndImpact - success path")
    void postRequestCompanyActivitiesAndImpactSuccess() throws Exception {

        CompanyAccountsDataState companyAccountsDataState = new CompanyAccountsDataState();
        companyAccountsDataState.setIsCic(true);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_DATA_STATE, companyAccountsDataState);

        when(companyActivitiesAndImpactService
                .submitCompanyActivitiesAndImpact(anyString(), anyString(),
                        any(CompanyActivitiesAndImpact.class))).thenReturn(new ArrayList<>());

        when(navigatorService.getNextControllerRedirect(
                any(Class.class),
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(
                        post(COMPANY_ACTIVITY_PATH).session(session).param("activitiesAndImpact", "value"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Accept CompanyActivitiesAndImpact - service exception")
    void postRequestCompanyActivitiesAndImpactServiceException() throws Exception {

        doThrow(ServiceException.class)
                .when(companyActivitiesAndImpactService)
                .submitCompanyActivitiesAndImpact(anyString(), anyString(),
                        any(CompanyActivitiesAndImpact.class));
        this.mockMvc.perform(post(COMPANY_ACTIVITY_PATH).param("activitiesAndImpact", "value"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Accept CompanyActivitiesAndImpact - binding errors")
    void postRequestCompanyActivitiesAndImpactBindingErrors() throws Exception {

        this.mockMvc.perform(post(COMPANY_ACTIVITY_PATH).param("activitiesAndImpact", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(COMPANY_ACTIVITY_VIEW));
    }


}
