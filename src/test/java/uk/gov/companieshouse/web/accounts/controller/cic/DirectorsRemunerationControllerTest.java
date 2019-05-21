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
import uk.gov.companieshouse.web.accounts.model.cic.statements.DirectorsRemuneration;
import uk.gov.companieshouse.web.accounts.service.cic.statements.DirectorsRemunerationService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DirectorsRemunerationControllerTest {

    public static final String DIRECTORS_REMUNERATION_PARAM = "directorsRemuneration";
    private MockMvc mockMvc;

    @Mock
    private DirectorsRemunerationService directorsRemunerationService;

    @Mock
    NavigatorService navigatorService;

    @InjectMocks
    private DirectorsRemunerationController directorsRemunerationController;

    private static final String DIRECTORS_REMUNERATION_PATH = "/company/012/transaction/345/company-accounts/678/cic/directors-remuneration";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String DIRECTORS_REMUNERATION_MODEL_ATTR = "directorsRemuneration";

    private static final String DIRECTORS_REMUNERATION_VIEW = "cic/directorsRemuneration";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH =
        UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(directorsRemunerationController)
            .build();
    }

    @Test
    @DisplayName("Get DirectorsRemuneration view - success path")
    void getRequestDirectorsRemunerationSuccess() throws Exception {

        when(directorsRemunerationService
            .getDirectorsRemuneration(anyString(), anyString()))
            .thenReturn(new DirectorsRemuneration());

        this.mockMvc.perform(get(DIRECTORS_REMUNERATION_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(DIRECTORS_REMUNERATION_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(DIRECTORS_REMUNERATION_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get DirectorsRemuneration view - service exception")
    void getRequestDirectorsRemunerationServiceException() throws Exception {

        when(directorsRemunerationService
            .getDirectorsRemuneration(anyString(), anyString()))
            .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(DIRECTORS_REMUNERATION_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Accept DirectorsRemuneration - success path")
    void postRequestDirectorsRemunerationSuccess() throws Exception {

        when(directorsRemunerationService
            .submitDirectorsRemuneration(anyString(), anyString(),
                any(DirectorsRemuneration.class))).thenReturn(new ArrayList<>());

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any()))
            .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc
            .perform(post(DIRECTORS_REMUNERATION_PATH).param(DIRECTORS_REMUNERATION_PARAM, "value"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Accept DirectorsRemuneration - service exception")
    void postRequestDirectorsRemunerationServiceException() throws Exception {

        doThrow(ServiceException.class)
            .when(directorsRemunerationService)
            .submitDirectorsRemuneration(anyString(), anyString(),
                any(DirectorsRemuneration.class));
        this.mockMvc
            .perform(post(DIRECTORS_REMUNERATION_PATH).param(DIRECTORS_REMUNERATION_PARAM, "value"))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Accept DirectorsRemuneration - binding errors")
    void postRequestDirectorsRemunerationBindingErrors() throws Exception {

        this.mockMvc
            .perform(post(DIRECTORS_REMUNERATION_PATH).param(DIRECTORS_REMUNERATION_PARAM, ""))
            .andExpect(status().isOk())
            .andExpect(view().name(DIRECTORS_REMUNERATION_VIEW));
    }


}
