package uk.gov.companieshouse.web.accounts.validation.smallfull.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.validation.smallfull.ValidateTurnoverPolicy;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidateTurnoverPolicyImplTest {

    @Mock
    private TurnoverPolicy turnoverPolicy;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private ValidateTurnoverPolicyImpl validateTurnoverPolicy;

    private static final String DETAILS = "turnoverPolicy";

    @BeforeEach
    private void setup() {

        turnoverPolicy = new TurnoverPolicy();
    }

    @Test
    @DisplayName("Turnover policy - No details provided")
    void turnoverPolicyWithNoDetailsProvided() {

        TurnoverPolicy policy = new TurnoverPolicy();
        policy.setIsIncludeTurnoverSelected(true);
        assertFalse(validateTurnoverPolicy.isValid(policy, constraintValidatorContext));
    }

    @Test
    @DisplayName("Turnover policy - With details provided")
    void turnoverPolicyWithDetailsProvided() {

        TurnoverPolicy policy = new TurnoverPolicy();
        policy.setIsIncludeTurnoverSelected(true);
        policy.setTurnoverPolicyDetails(DETAILS);

        assertTrue(validateTurnoverPolicy.isValid(policy, constraintValidatorContext));
    }
}