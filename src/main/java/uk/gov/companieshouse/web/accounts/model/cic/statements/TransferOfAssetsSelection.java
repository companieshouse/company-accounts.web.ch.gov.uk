package uk.gov.companieshouse.web.accounts.model.cic.statements;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferOfAssetsSelection {

    @NotNull(message = "{transferOfAssetsSelection.selectionNotMade}")
    private Boolean hasProvidedTransferOfAssets;
}
