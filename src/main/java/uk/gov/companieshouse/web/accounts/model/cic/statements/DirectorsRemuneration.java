package uk.gov.companieshouse.web.accounts.model.cic.statements;

import javax.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class DirectorsRemuneration {

    @NotBlank(message = "{directorsRemuneration.directorsRemuneration.missing}")
    @ValidationMapping("$.cic_statements.report_statements.directors_remuneration")
    private String directorsRemuneration;

    public String getDirectorsRemuneration() {
        return directorsRemuneration;
    }

    public void setDirectorsRemuneration(String directorsRemuneration) {
        this.directorsRemuneration = directorsRemuneration;
    }
}
