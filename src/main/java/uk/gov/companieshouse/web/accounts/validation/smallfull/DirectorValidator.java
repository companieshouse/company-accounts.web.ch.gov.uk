package uk.gov.companieshouse.web.accounts.validation.smallfull;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Component
public class DirectorValidator {

    private static final String WAS_DIRECTOR_APPOINTED = "directorToAdd.wasDirectorAppointedDuringPeriod";
    private static final String APPOINTED_NOT_SELECTED = "validation.directorToAdd.appointment.selectionNotMade";

    private static final String DID_DIRECTOR_RESIGN = "directorToAdd.didDirectorResignDuringPeriod";
    private static final String RESIGNATION_NOT_SELECTED = "validation.directorToAdd.resignation.selectionNotMade";

    public List<ValidationError> validateDirectorToAdd(DirectorToAdd directorToAdd) {

        List<ValidationError> validationErrors = new ArrayList<>();

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
}
