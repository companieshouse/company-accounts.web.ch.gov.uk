package uk.gov.companieshouse.web.accounts.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;

@Component
public class DateValidator {
    private static final String DAY_MONTH_REGEX = "\\d{1,2}";

    private static final String YEAR_REGEX = "\\d{4}";

    private static final String DATE_MISSING = "validation.date.missing";

    private static final String DATE_INCOMPLETE = "validation.date.incomplete";

    private static final String DATE_FORMAT_INVALID = "validation.date.format";

    private static final String DATE_INVALID = "validation.date.nonExistent";

    public List<ValidationError> validateDate(Date date, String fieldPath, String jsonPathSuffix) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(date.getDay()) &&
                StringUtils.isBlank(date.getMonth()) &&
                StringUtils.isBlank(date.getYear())) {
            ValidationError error = new ValidationError();
            error.setFieldPath(fieldPath);
            error.setMessageKey(DATE_MISSING + jsonPathSuffix);
            validationErrors.add(error);

        } else if (StringUtils.isBlank(date.getDay()) ||
                StringUtils.isBlank(date.getMonth()) ||
                StringUtils.isBlank(date.getYear())) {
            ValidationError error = new ValidationError();
            error.setFieldPath(fieldPath);
            error.setMessageKey(DATE_INCOMPLETE + jsonPathSuffix);
            validationErrors.add(error);

        } else if (!date.getDay().matches(DAY_MONTH_REGEX) ||
                !date.getMonth().matches(DAY_MONTH_REGEX) ||
                !date.getYear().matches(YEAR_REGEX)) {
            ValidationError error = new ValidationError();
            error.setFieldPath(fieldPath);
            error.setMessageKey(DATE_FORMAT_INVALID + jsonPathSuffix);
            validationErrors.add(error);

        } else {
            try {
                LocalDate.parse(date.getYear() + "-" + date.getMonth() + "-" + date.getDay(),
                        DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT));
            } catch (DateTimeParseException e) {
                ValidationError error = new ValidationError();
                error.setFieldPath(fieldPath);
                error.setMessageKey(DATE_INVALID);
                validationErrors.add(error);
            }
        }

        return validationErrors;
    }
}
