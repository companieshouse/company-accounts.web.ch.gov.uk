package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class FixedAssets {

    private TangibleAssets tangibleAssets;

    @ValidationMapping("$.current_period.balance_sheet.fixed_assets.total")
    private Long totalFixedAssets;

}
