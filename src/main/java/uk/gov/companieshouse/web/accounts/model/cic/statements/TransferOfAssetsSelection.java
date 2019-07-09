package uk.gov.companieshouse.web.accounts.model.cic.statements;

import javax.validation.constraints.NotNull;

public class TransferOfAssetsSelection {

    @NotNull(message = "{transferOfAssetsSelection.selectionNotMade}")
    private Boolean hasProvidedTransferOfAssets;

    public Boolean getHasProvidedTransferOfAssets() {
        return hasProvidedTransferOfAssets;
    }

    public void setHasProvidedTransferOfAssets(Boolean hasProvidedTransferOfAssets) {
        this.hasProvidedTransferOfAssets = hasProvidedTransferOfAssets;
    }
}
