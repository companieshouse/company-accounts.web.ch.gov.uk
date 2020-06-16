package uk.gov.companieshouse.web.accounts.validation.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DirectorValidatorTest {

    @Mock
    SmallFullService smallFullService;

    @Mock
    ApiClientService apiClientService;

    @Mock
    ApiClient apiClient;

    @InjectMocks
    DirectorValidator validator;

    private static final String DIRECTOR_NAME = "directorName";

    private static final String DIRECTOR_TO_ADD = "directorToAdd";
    private static final String DIRECTOR_MUST_BE_ADDED = "validation.directorToAdd.submissionRequired";
    private static final String AT_LEAST_ONE_DIRECTOR_REQUIRED = "validation.addOrRemoveDirectors.oneRequired";

    private static final String NAME = DIRECTOR_TO_ADD + ".name";
    private static final String NAME_NOT_PRESENT = "validation.element.missing.director.name";

    private static final String WAS_DIRECTOR_APPOINTED = DIRECTOR_TO_ADD + ".wasDirectorAppointedDuringPeriod";
    private static final String APPOINTED_NOT_SELECTED = "validation.directorToAdd.appointment.selectionNotMade";

    private static final String DID_DIRECTOR_RESIGN = DIRECTOR_TO_ADD + ".didDirectorResignDuringPeriod";
    private static final String RESIGNATION_NOT_SELECTED = "validation.directorToAdd.resignation.selectionNotMade";

    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String OUTSIDE_VALID_DATE_RANGE = "validation.date.outside.currentPeriod.accounting_period";

    private static final LocalDate NEXT_ACCOUNTS_PERIOD_START = LocalDate.of(2019, 1, 1);
    private static final LocalDate NEXT_ACCOUNTS_PERIOD_END = LocalDate.of(2019, 12, 31);

    private static final LocalDate VALID_APPOINTMENT_DATE = NEXT_ACCOUNTS_PERIOD_START.plusDays(1);

    @Test
    @DisplayName("Validate director to add - success")
    void validateDirectorToAddSuccess() {

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setName(DIRECTOR_NAME);
        directorToAdd.setWasDirectorAppointedDuringPeriod(true);
        directorToAdd.setDidDirectorResignDuringPeriod(false);

        List<ValidationError> validationErrors = validator.validateDirectorToAdd(directorToAdd);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate director to add - missing name")
    void validateDirectorToAddMissingName() {

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setWasDirectorAppointedDuringPeriod(true);
        directorToAdd.setDidDirectorResignDuringPeriod(false);

        List<ValidationError> validationErrors = validator.validateDirectorToAdd(directorToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(NAME, validationErrors.get(0).getFieldPath());
        assertEquals(NAME_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate director to add - missing was director appointed")
    void validateDirectorToAddMissingWasDirectorAppointed() {

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setName(DIRECTOR_NAME);
        directorToAdd.setDidDirectorResignDuringPeriod(false);

        List<ValidationError> validationErrors = validator.validateDirectorToAdd(directorToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(WAS_DIRECTOR_APPOINTED, validationErrors.get(0).getFieldPath());
        assertEquals(APPOINTED_NOT_SELECTED, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate director to add - missing did director resign")
    void validateDirectorToAddMissingDidDirectorResign() {

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setName(DIRECTOR_NAME);
        directorToAdd.setWasDirectorAppointedDuringPeriod(true);

        List<ValidationError> validationErrors = validator.validateDirectorToAdd(directorToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DID_DIRECTOR_RESIGN, validationErrors.get(0).getFieldPath());
        assertEquals(RESIGNATION_NOT_SELECTED, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate submit add or remove directors - success")
    void validateSubmitAddOrRemoveDirectorsSuccess() throws ServiceException {

        SmallFullApi smallFullApi = new SmallFullApi();
        smallFullApi.setNextAccounts(createNextAccounts());

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        AddOrRemoveDirectors addOrRemoveDirectors = new AddOrRemoveDirectors();
        addOrRemoveDirectors.setExistingDirectors(new Director[]{new Director()});
        addOrRemoveDirectors.setDirectorToAdd(new DirectorToAdd());

        List<ValidationError> validationErrors = validator.validateSubmitAddOrRemoveDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveDirectors);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate submit add or remove directors - uncommitted director name")
    void validateSubmitAddOrRemoveDirectorsUncommittedDirectorName() throws ServiceException {

        AddOrRemoveDirectors addOrRemoveDirectors = new AddOrRemoveDirectors();

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setName(DIRECTOR_NAME);
        addOrRemoveDirectors.setDirectorToAdd(directorToAdd);

        List<ValidationError> validationErrors = validator.validateSubmitAddOrRemoveDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveDirectors);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DIRECTOR_TO_ADD, validationErrors.get(0).getFieldPath());
        assertEquals(DIRECTOR_MUST_BE_ADDED, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate submit add or remove directors - no directors present")
    void validateSubmitAddOrRemoveDirectorsNoDirectorsPresent() throws ServiceException {

        AddOrRemoveDirectors addOrRemoveDirectors = new AddOrRemoveDirectors();
        addOrRemoveDirectors.setDirectorToAdd(new DirectorToAdd());

        List<ValidationError> validationErrors = validator.validateSubmitAddOrRemoveDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveDirectors);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DIRECTOR_TO_ADD, validationErrors.get(0).getFieldPath());
        assertEquals(AT_LEAST_ONE_DIRECTOR_REQUIRED, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate submit add or remove directors - all directors have resigned")
    void validateSubmitAddOrRemoveDirectorsAllDirectorsHaveResigned() throws ServiceException {

        SmallFullApi smallFullApi = new SmallFullApi();
        smallFullApi.setNextAccounts(createNextAccounts());

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        AddOrRemoveDirectors addOrRemoveDirectors = new AddOrRemoveDirectors();
        Director director = new Director();
        director.setResignationDate(VALID_APPOINTMENT_DATE);
        addOrRemoveDirectors.setExistingDirectors(new Director[]{director});
        addOrRemoveDirectors.setDirectorToAdd(new DirectorToAdd());

        List<ValidationError> validationErrors = validator.validateSubmitAddOrRemoveDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveDirectors);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DIRECTOR_TO_ADD, validationErrors.get(0).getFieldPath());
        assertEquals(AT_LEAST_ONE_DIRECTOR_REQUIRED, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate submit add or remove directors - Change ARD invalid date")
    void validateSubmitAddOrRemoveDirectorsArdChanges() throws ServiceException {
        SmallFullApi smallFullApi = new SmallFullApi();
        smallFullApi.setNextAccounts(createNextAccounts());

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        AddOrRemoveDirectors addOrRemoveDirectors = new AddOrRemoveDirectors();
        Director director = new Director();
        director.setAppointmentDate(NEXT_ACCOUNTS_PERIOD_END.plusDays(1));
        addOrRemoveDirectors.setExistingDirectors(new Director[]{director});
        addOrRemoveDirectors.setDirectorToAdd(new DirectorToAdd());

        List<ValidationError> validationErrors = validator.validateSubmitAddOrRemoveDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveDirectors);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals("", validationErrors.get(0).getFieldPath());
        assertEquals(OUTSIDE_VALID_DATE_RANGE, validationErrors.get(0).getMessageKey());
    }

    private AccountingPeriodApi createNextAccounts() {
        AccountingPeriodApi nextAccounts = new AccountingPeriodApi();

        nextAccounts.setPeriodStartOn(NEXT_ACCOUNTS_PERIOD_START);
        nextAccounts.setPeriodEndOn(NEXT_ACCOUNTS_PERIOD_END);

        return nextAccounts;
    }
}
