package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddOrRemoveDirectorsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DirectorService directorService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MockHttpSession session;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private DirectorToAdd directorToAdd;

    @Mock
    private AddOrRemoveDirectors addOrRemoveDirectors;

    @InjectMocks
    private AddOrRemoveDirectorsController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String DIRECTOR_ID = "directorId";

    private static final String ADD_OR_REMOVE_DIRECTORS_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/add-or-remove-directors";

    private static final String REMOVE_DIRECTOR_PATH = ADD_OR_REMOVE_DIRECTORS_PATH + "/remove/" + DIRECTOR_ID;

    private static final String ADD_DIRECTOR_PATH = ADD_OR_REMOVE_DIRECTORS_PATH + "/add/add-director";


    private static final String ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR = "addOrRemoveDirectors";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String ADD_OR_REMOVE_DIRECTORS_VIEW = "smallfull/addOrRemoveDirectors";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get add or remove directors view - success path")
    void getRequestSuccess() throws Exception {

        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(new Director[0]);

        this.mockMvc.perform(get(ADD_OR_REMOVE_DIRECTORS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_DIRECTORS_VIEW))
                .andExpect(model().attributeExists(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_NUMBER))
                .andExpect(model().attributeExists(TRANSACTION_ID))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get add or remove directors view - service exception")
    void getRequestServiceException() throws Exception {

        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(ADD_OR_REMOVE_DIRECTORS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post add director - throws service exception")
    void postDirectorAddRequestThrowsServiceException() throws Exception {


        when(directorService.createDirector(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorToAdd.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(ADD_DIRECTOR_PATH)
                .param("wasDirectorAppointedDuringPeriod", "0")
                .param("didDirectorResignDuringPeriod", "0")
                .param("name", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post add director - success")
    void postDirectorAddRequestSuccess() throws Exception {


        when(directorService.createDirector(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorToAdd.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        this.mockMvc.perform(post(ADD_DIRECTOR_PATH)
                .param("wasDirectorAppointedDuringPeriod", "0")
                .param("didDirectorResignDuringPeriod", "0")
                .param("name", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + ADD_OR_REMOVE_DIRECTORS_PATH));
    }

    @Test
    @DisplayName("Post add director - throws validation errors")
    void postDirectorAddRequestThrowsValidationErrors() throws Exception {


        when(directorService.createDirector(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorToAdd.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(post(ADD_DIRECTOR_PATH)
                .param("wasDirectorAppointedDuringPeriod", "0")
                .param("didDirectorResignDuringPeriod", "0")
                .param("name", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_DIRECTORS_VIEW));
    }

    @Test
    @DisplayName("Post add director - has binding errors")
    void postDirectorAddRequestHasBindingErrors() throws Exception {

        String beanElement = "director.name";
        // Mock non-numeric input to trigger binding result errors
        String invalidData = "1";

        this.mockMvc.perform(post(ADD_DIRECTOR_PATH)
                .param(beanElement, invalidData))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_DIRECTORS_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));;
    }

    @Test
    @DisplayName("Delete director - success path")
    void deleteDirectorSuccess() throws Exception {

        this.mockMvc.perform(get(REMOVE_DIRECTOR_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + ADD_OR_REMOVE_DIRECTORS_PATH));

        verify(directorService).deleteDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, DIRECTOR_ID);
    }

    @Test
    @DisplayName("Delete director - service exception")
    void deleteDirectorServiceException() throws Exception {

        doThrow(ServiceException.class).when(directorService).deleteDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, DIRECTOR_ID);

        this.mockMvc.perform(get(REMOVE_DIRECTOR_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post add or remove directors view - success path")
    void postRequestSuccess() throws Exception {

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(ADD_OR_REMOVE_DIRECTORS_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }


    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedDirectorsReport()).thenReturn(false);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedDirectorsReport()).thenReturn(true);

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
