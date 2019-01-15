package uk.gov.companieshouse.web.accounts.util.navigator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.MissingAnnotationException;
import uk.gov.companieshouse.web.accounts.util.Navigator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NavigatorTests {

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private Navigator navigator;

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Test
    public void missingNextControllerAnnotation() {
        Throwable exception = assertThrows(MissingAnnotationException.class, () ->
                navigator.getNextControllerRedirect(MockControllerThree.class, COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        assertEquals("Missing @NextController annotation on class uk.gov.companieshouse.web.accounts.util.navigator.MockControllerThree", exception.getMessage());
    }

    @Test
    public void missingPreviousControllerAnnotation() {
        Throwable exception = assertThrows(MissingAnnotationException.class, () ->
                navigator.getPreviousControllerPath(MockControllerThree.class, COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        assertEquals("Missing @PreviousController annotation on class uk.gov.companieshouse.web.accounts.util.navigator.MockControllerThree", exception.getMessage());
    }

    @Test
    public void missingRequestMappingAnnotationOnNextController() {
        Throwable exception = assertThrows(MissingAnnotationException.class, () ->
                navigator.getNextControllerRedirect(MockControllerOne.class, COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        assertEquals("Missing @RequestMapping annotation on class uk.gov.companieshouse.web.accounts.util.navigator.MockControllerTwo", exception.getMessage());
    }

    @Test
    public void missingRequestMappingAnnotationOnPreviousController() {
        Throwable exception = assertThrows(MissingAnnotationException.class, () ->
                navigator.getPreviousControllerPath(MockControllerTwo.class, COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        assertEquals("Missing @RequestMapping annotation on class uk.gov.companieshouse.web.accounts.util.navigator.MockControllerOne", exception.getMessage());
    }

    @Test
    public void missingExpectedNumberOfPathVariablesForMandatoryController() {

        String redirect = navigator.getNextControllerRedirect(MockControllerFour.class, COMPANY_NUMBER);

        assertEquals(UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/mock-controller-five", redirect);
    }

    @Test
    public void successfulRedirectWithSingleConditionalControllerInChain() {
        when(applicationContext.getBean(MockSuccessJourneyControllerTwo.class)).thenReturn(new MockSuccessJourneyControllerTwo());
        when(applicationContext.getBean(MockSuccessJourneyControllerThree.class)).thenReturn(new MockSuccessJourneyControllerThree());

        String redirect = navigator.getNextControllerRedirect(MockSuccessJourneyControllerOne.class, COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/mock-success-journey-controller-three", redirect);
    }
}