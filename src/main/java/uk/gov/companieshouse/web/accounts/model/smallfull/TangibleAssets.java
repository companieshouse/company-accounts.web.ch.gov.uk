package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class TangibleAssets {

    @ValidationMapping("$.current_period.balance_sheet.fixed_assets.tangible")
    private Integer currentAmount;

}
