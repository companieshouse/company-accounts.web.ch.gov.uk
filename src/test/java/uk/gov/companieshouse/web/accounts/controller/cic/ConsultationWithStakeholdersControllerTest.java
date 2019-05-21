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
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.ConsultationWithStakeholders;
import uk.gov.companieshouse.web.accounts.service.cic.statements.ConsultationWithStakeholdersService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConsultationWithStakeholdersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ConsultationWithStakeholdersService consultationWithStakeholdersService;

    @Mock
    NavigatorService navigatorService;

    @InjectMocks
    private ConsultationWithStakeholdersController consultationWithStakeholdersController;

    private static final String COMPANY_ACTIVITY_PATH = "/company/012/transaction/345/company-accounts/678/cic/consultation";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String CONSULTATION_MODEL_ATTR = "consultationWithStakeholders";

    private static final String CONSULTATION_VIEW = "cic/consultationWithStakeholders";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH =
        UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(consultationWithStakeholdersController)
            .build();
    }

    @Test
    @DisplayName("Get ConsultationWithStakeholders view - success path")
    void getRequestConsultationWithStakeholdersSuccess() throws Exception {

        when(consultationWithStakeholdersService
            .getConsultationWithStakeholders(anyString(), anyString()))
            .thenReturn(new ConsultationWithStakeholders());

        this.mockMvc.perform(get(COMPANY_ACTIVITY_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(CONSULTATION_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(CONSULTATION_MODEL_ATTR));
    }


    @Test
    @DisplayName("Get ConsultationWithStakeholders view - service exception")
    void getRequestConsultationWithStakeholdersServiceException() throws Exception {

        when(consultationWithStakeholdersService
            .getConsultationWithStakeholders(anyString(), anyString()))
            .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(COMPANY_ACTIVITY_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Accept ConsultationWithStakeholders - success path")
    void postRequestConsultationWithStakeholdersSuccess() throws Exception {

        when(consultationWithStakeholdersService
            .submitConsultationWithStakeholders(anyString(), anyString(),
                any(ConsultationWithStakeholders.class))).thenReturn(new ArrayList<>());

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any()))
            .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(COMPANY_ACTIVITY_PATH).param("consultationWithStakeholders", "value"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Accept ConsultationWithStakeholders - service exception")
    void postRequestConsultationWithStakeholdersServiceException() throws Exception {

        doThrow(ServiceException.class)
            .when(consultationWithStakeholdersService)
            .submitConsultationWithStakeholders(anyString(), anyString(),
                any(ConsultationWithStakeholders.class));
        this.mockMvc.perform(post(COMPANY_ACTIVITY_PATH).param("consultationWithStakeholders", "value"))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Accept ConsultationWithStakeholders - binding errors")
    void postRequestConsultationWithStakeholdersBindingErrors() throws Exception {

        this.mockMvc.perform(post(COMPANY_ACTIVITY_PATH).param("consultationWithStakeholders", ""))
            .andExpect(status().isOk())
            .andExpect(view().name(CONSULTATION_VIEW));
    }


}
