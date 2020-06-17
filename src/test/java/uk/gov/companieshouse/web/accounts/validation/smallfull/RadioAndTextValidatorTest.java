package uk.gov.companieshouse.web.accounts.validation.smallfull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.items.Turnover;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class RadioAndTextValidatorTest {

    @Mock
    private BindingResult bindingResult;

    private static final String ERROR_LOCATION = "turnoverError";
    private static final String ERROR_MESSAGE = "turnoverErrorMessage";

    @Test
    void veyValidateMethod() {

        RadioAndTextValidator radioAndTextValidator = new RadioAndTextValidator();
        TurnoverPolicy turnoverPolicy = new TurnoverPolicy();

        turnoverPolicy.setIsIncludeTurnoverSelected(true);

        radioAndTextValidator.validate(turnoverPolicy.getIsIncludeTurnoverSelected(), turnoverPolicy.getTurnoverPolicyDetails(), bindingResult, ERROR_MESSAGE, ERROR_LOCATION);

        verify(bindingResult).rejectValue( ERROR_LOCATION, ERROR_MESSAGE, null, null);

    }


}