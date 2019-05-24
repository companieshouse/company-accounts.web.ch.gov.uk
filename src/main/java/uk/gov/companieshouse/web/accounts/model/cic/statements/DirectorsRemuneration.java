package uk.gov.companieshouse.web.accounts.model.cic.statements;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class DirectorsRemuneration {

    @NotBlank(message = "{directorsRemuneration.directorsRemuneration.missing}")
    @ValidationMapping("$.cic_statements.report_statements.directors_remuneration")
    private String directorsRemuneration;
}
