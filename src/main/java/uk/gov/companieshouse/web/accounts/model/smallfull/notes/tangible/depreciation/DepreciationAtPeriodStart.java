package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class DepreciationAtPeriodStart {

    @ValidationMapping("$.tangible_assets.land_and_buildings.depreciation.at_period_start")
    private Long landAndBuildings;

    @ValidationMapping("$.tangible_assets.plant_and_machinery.depreciation.at_period_start")
    private Long plantAndMachinery;

    @ValidationMapping("$.tangible_assets.fixtures_and_fittings.depreciation.at_period_start")
    private Long fixturesAndFittings;

    @ValidationMapping("$.tangible_assets.office_equipment.depreciation.at_period_start")
    private Long officeEquipment;

    @ValidationMapping("$.tangible_assets.motor_vehicles.depreciation.at_period_start")
    private Long motorVehicles;

    @ValidationMapping("$.tangible_assets.total.depreciation.at_period_start")
    private Long total;
}
