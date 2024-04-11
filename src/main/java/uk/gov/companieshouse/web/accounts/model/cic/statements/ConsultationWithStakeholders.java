package uk.gov.companieshouse.web.accounts.model.cic.statements;

import jakarta.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class ConsultationWithStakeholders {
    @NotBlank(message = "{consultationWithStakeholders.consultationWithStakeholders.missing}")
    @ValidationMapping("$.cic_statements.report_statements.consultation_with_stakeholders")
    private String consultationWithStakeholders;

    public String getConsultationWithStakeholders() {
        return consultationWithStakeholders;
    }

    public void setConsultationWithStakeholders(String consultationWithStakeholders) {
        this.consultationWithStakeholders = consultationWithStakeholders;
    }
}
