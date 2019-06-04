package uk.gov.companieshouse.web.accounts.model.cic.statements;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class ConsultationWithStakeholders {

    @NotBlank(message = "{consultationWithStakeholders.consultationWithStakeholders.missing}")
    @ValidationMapping("$.cic_statements.report_statements.consultation_with_stakeholders")
    private String consultationWithStakeholders;
}
