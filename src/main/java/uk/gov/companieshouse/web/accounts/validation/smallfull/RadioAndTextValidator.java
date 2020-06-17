package uk.gov.companieshouse.web.accounts.validation.smallfull;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class RadioAndTextValidator {

    public void validate(Boolean selection, String text, BindingResult binding, String error, String location) {
        if (BooleanUtils.isTrue(selection) && StringUtils.isBlank(text)) {

            binding.rejectValue(location,
                    error,
                    null,
                    null);
        }
    }
}
