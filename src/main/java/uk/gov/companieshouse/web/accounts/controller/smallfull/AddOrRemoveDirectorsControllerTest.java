package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;

public class AddOrRemoveDirectorsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DirectorService directorService;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private AddOrRemoveDirectorsController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ADD_OR_REMOVE_DIRECTORS_PATH = "/company/" + COMPANY_NUMBER +
                                                                "/transaction/" + TRANSACTION_ID +
                                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                                "/small-full/add-or-remove-directors";

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
}
