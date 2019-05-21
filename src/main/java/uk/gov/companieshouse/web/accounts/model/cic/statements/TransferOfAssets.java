package uk.gov.companieshouse.web.accounts.model.cic.statements;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class TransferOfAssets {

    @NotBlank(message = "{transferOfAssets.transferOfAssets.missing}")
    @ValidationMapping("$.cic_statements.report_statements.transfer_of_assets")
    private String transferOfAssets;
}
