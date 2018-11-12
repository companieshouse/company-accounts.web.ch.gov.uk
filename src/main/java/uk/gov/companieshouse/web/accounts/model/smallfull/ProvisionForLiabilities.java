package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class ProvisionForLiabilities {

    @ValidationMapping("$.current_period.balance_sheet.other_liabilities_or_assets.provision_for_liabilities")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.other_liabilities_or_assets.provision_for_liabilities")
    private Long previousAmount;
}
