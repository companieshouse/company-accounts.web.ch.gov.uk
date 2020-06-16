package uk.gov.companieshouse.web.accounts.validation.smallfull.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidateTangibleDepreciationPolicyImplTest {

    @Mock
    private TangibleDepreciationPolicy tangibleDepreciationPolicy;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private ValidateTangibleDepreciationPolicyImpl validateTangibleDepreciationPolicy;

    private static final String DETAILS = "tangibleDepreciationPolicy";

    @BeforeEach
    private void setup() {

        tangibleDepreciationPolicy = new TangibleDepreciationPolicy();
    }

    @Test
    @DisplayName("TangibleDepreciationPolicy - No details provided")
    void turnoverPolicyWithNoDetailsProvided() {

        tangibleDepreciationPolicy.setHasTangibleDepreciationPolicySelected(true);
        assertFalse(validateTangibleDepreciationPolicy.isValid(tangibleDepreciationPolicy, constraintValidatorContext));
    }

    @Test
    @DisplayName("TangibleDepreciationPolicy - With details provided")
    void turnoverPolicyWithDetailsProvided() {

        tangibleDepreciationPolicy.setHasTangibleDepreciationPolicySelected(true);
        tangibleDepreciationPolicy.setTangibleDepreciationPolicyDetails(DETAILS);

        assertTrue(validateTangibleDepreciationPolicy.isValid(tangibleDepreciationPolicy, constraintValidatorContext));
    }
}