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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.statements.TransferOfAssets;
import uk.gov.companieshouse.web.accounts.service.cic.statements.TransferOfAssetsService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransferOfAssetsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransferOfAssetsService transferOfAssetsService;

    @Mock
    NavigatorService navigatorService;

    @InjectMocks
    private TransferOfAssetsController transferOfAssetsController;

    private static final String TRANSFER_OF_ASSETS_PATH = "/company/012/transaction/345/company-accounts/678/cic/transfer-of-assets";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String TRANSFER_MODEL_ATTR = "transferOfAssets";

    private static final String TRANSFER_VIEW = "cic/transferOfAssets";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    public void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(transferOfAssetsController)
                .build();
    }

    @Test
    @DisplayName("Get ConsultationWithStakeholders view - success path")
    void getRequestConsultationWithStakeholdersSuccess() throws Exception {

        when(transferOfAssetsService
                .getTransferOfAssets(anyString(), anyString()))
                .thenReturn(new TransferOfAssets());

        this.mockMvc.perform(get(TRANSFER_OF_ASSETS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(TRANSFER_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(TRANSFER_MODEL_ATTR));
    }


    @Test
    @DisplayName("Get ConsultationWithStakeholders view - service exception")
    void getRequestConsultationWithStakeholdersServiceException() throws Exception {

        when(transferOfAssetsService
                .getTransferOfAssets(anyString(), anyString()))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(TRANSFER_OF_ASSETS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Accept ConsultationWithStakeholders - success path")
    void postRequestConsultationWithStakeholdersSuccess() throws Exception {

        when(transferOfAssetsService
                .submitTransferOfAssets(anyString(), anyString(),
                        any(TransferOfAssets.class))).thenReturn(new ArrayList<>());

        when(navigatorService.getNextControllerRedirect(
                any(Class.class),
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(TRANSFER_OF_ASSETS_PATH).param("transferOfAssets", "value"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Accept ConsultationWithStakeholders - service exception")
    void postRequestConsultationWithStakeholdersServiceException() throws Exception {

        doThrow(ServiceException.class)
                .when(transferOfAssetsService)
                .submitTransferOfAssets(anyString(), anyString(),
                        any(TransferOfAssets.class));
        this.mockMvc.perform(post(TRANSFER_OF_ASSETS_PATH).param("transferOfAssets", "value"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Accept ConsultationWithStakeholders - binding errors")
    void postRequestConsultationWithStakeholdersBindingErrors() throws Exception {

        this.mockMvc.perform(post(TRANSFER_OF_ASSETS_PATH).param("transferOfAssets", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(TRANSFER_VIEW));
    }


}
