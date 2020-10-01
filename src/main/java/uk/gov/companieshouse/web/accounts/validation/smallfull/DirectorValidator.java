package uk.gov.companieshouse.web.accounts.validation.smallfull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Component
public class DirectorValidator {

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ApiClientService apiClientService;

    private static final String DIRECTOR_TO_ADD = "directorToAdd";
    private static final String DIRECTOR_MUST_BE_ADDED = "validation.directorToAdd.submissionRequired";
    private static final String AT_LEAST_ONE_DIRECTOR_REQUIRED = "validation.addOrRemoveDirectors.oneRequired";

    private static final String NAME = DIRECTOR_TO_ADD + ".name";
    private static final String NAME_NOT_PRESENT = "validation.element.missing.director.name";

    private static final String WAS_DIRECTOR_APPOINTED = DIRECTOR_TO_ADD + ".wasDirectorAppointedDuringPeriod";
    private static final String APPOINTED_NOT_SELECTED = "validation.directorToAdd.appointment.selectionNotMade";

    private static final String DID_DIRECTOR_RESIGN = DIRECTOR_TO_ADD + ".didDirectorResignDuringPeriod";
    private static final String RESIGNATION_NOT_SELECTED = "validation.directorToAdd.resignation.selectionNotMade";

    private static final String OUTSIDE_VALID_DATE_RANGE = "validation.date.outside.currentPeriod.accounting_period";

    public List<ValidationError> validateDirectorToAdd(DirectorToAdd directorToAdd) {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(directorToAdd.getName())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(NAME);
            error.setMessageKey(NAME_NOT_PRESENT);
            validationErrors.add(error);
        }

        if (directorToAdd.getWasDirectorAppointedDuringPeriod() == null) {

            ValidationError error = new ValidationError();
            error.setFieldPath(WAS_DIRECTOR_APPOINTED);
            error.setMessageKey(APPOINTED_NOT_SELECTED);
            validationErrors.add(error);
        }

        if (directorToAdd.getDidDirectorResignDuringPeriod() == null) {

            ValidationError error = new ValidationError();
            error.setFieldPath(DID_DIRECTOR_RESIGN);
            error.setMessageKey(RESIGNATION_NOT_SELECTED);
            validationErrors.add(error);
        }

        return validationErrors;
    }

    public List<ValidationError> validateSubmitAddOrRemoveDirectors(String transactionId,
                                                                    String companyAccountsId,
                                                                    AddOrRemoveDirectors addOrRemoveDirectors) throws ServiceException {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isNotBlank(Optional.ofNullable(addOrRemoveDirectors)
                                    .map(AddOrRemoveDirectors::getDirectorToAdd)
                                    .map(DirectorToAdd::getName)
                                    .orElse(null))) {

            ValidationError error = new ValidationError();
            error.setFieldPath(DIRECTOR_TO_ADD);
            error.setMessageKey(DIRECTOR_MUST_BE_ADDED);
            validationErrors.add(error);

        } else {
            assert addOrRemoveDirectors != null;
            if (addOrRemoveDirectors.getExistingDirectors() == null ||
                    Arrays.stream(addOrRemoveDirectors.getExistingDirectors())
                            .noneMatch(d -> d.getResignationDate() == null)) {

                ValidationError error = new ValidationError();
                error.setFieldPath(DIRECTOR_TO_ADD);
                error.setMessageKey(AT_LEAST_ONE_DIRECTOR_REQUIRED);
                validationErrors.add(error);
            }
        }

        if(addOrRemoveDirectors.getExistingDirectors() != null) {

            ApiClient apiClient = apiClientService.getApiClient();

            SmallFullApi smallFullApi = smallFullService
                    .getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

            LocalDate periodStartOn = smallFullApi.getNextAccounts().getPeriodStartOn();
            LocalDate periodEndOn = smallFullApi.getNextAccounts().getPeriodEndOn();

            for (Director director : addOrRemoveDirectors.getExistingDirectors()) {

                if (!isValidAppointmentOrResignationDate(director, periodStartOn, periodEndOn)) {
                    ValidationError error = new ValidationError();
                    error.setFieldPath("");
                    error.setMessageKey(OUTSIDE_VALID_DATE_RANGE);
                    validationErrors.add(error);

                    break;
                }
            }
        }

        return validationErrors;
    }

    private boolean isValidAppointmentOrResignationDate(Director director, LocalDate periodStartOn, LocalDate periodEndOn) {
        boolean isValid = true;

        if (director.getResignationDate() != null &&
                (director.getResignationDate().isBefore(periodStartOn) || director.getResignationDate().isAfter(periodEndOn))) {
            isValid = false;
        }
        if (director.getAppointmentDate() != null &&
                (director.getAppointmentDate().isBefore(periodStartOn) || director.getAppointmentDate().isAfter(periodEndOn))) {

            isValid = false;
        }

        return isValid;
    }
}
