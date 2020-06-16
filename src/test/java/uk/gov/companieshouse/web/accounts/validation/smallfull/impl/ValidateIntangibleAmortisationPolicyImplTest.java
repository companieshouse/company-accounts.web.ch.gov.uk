package uk.gov.companieshouse.web.accounts.validation.smallfull.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidateIntangibleAmortisationPolicyImplTest {

        @Mock
        private IntangibleAmortisationPolicy intangibleAmortisationPolicy;

        @Mock
        private ConstraintValidatorContext constraintValidatorContext;

        @InjectMocks
        private ValidateIntangibleAmortisationPolicyImpl validateIntangibleAmortisationPolicy;

        private static final String DETAILS = "intangibleAmortisationPolicy";

        @BeforeEach
        private void setup() {

            intangibleAmortisationPolicy = new IntangibleAmortisationPolicy();
        }

        @Test
        @DisplayName("IntangibleAmortisationPolicy - No details provided")
        void intangibleAmortisationPolicyWithNoDetailsProvided() {

            intangibleAmortisationPolicy.setIncludeIntangibleAmortisationPolicy(true);
            assertFalse(validateIntangibleAmortisationPolicy.isValid(intangibleAmortisationPolicy, constraintValidatorContext));
        }

        @Test
        @DisplayName("IntangibleAmortisationPolicy - With details provided")
        void turnoverPolicyWithDetailsProvided() {

            intangibleAmortisationPolicy.setIncludeIntangibleAmortisationPolicy(true);
            intangibleAmortisationPolicy.setIntangibleAmortisationPolicyDetails(DETAILS);

            assertTrue(validateIntangibleAmortisationPolicy.isValid(intangibleAmortisationPolicy, constraintValidatorContext));
        }
}