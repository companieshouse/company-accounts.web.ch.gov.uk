package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class OtherAdjustments {

    @ValidationMapping("$.tangible_assets.land_and_buildings.depreciation.other_adjustments")
    private Long landAndBuildings;

    @ValidationMapping("$.tangible_assets.plant_and_machinery.depreciation.other_adjustments")
    private Long plantAndMachinery;

    @ValidationMapping("$.tangible_assets.fixtures_and_fittings.depreciation.other_adjustments")
    private Long fixturesAndFittings;

    @ValidationMapping("$.tangible_assets.office_equipment.depreciation.other_adjustments")
    private Long officeEquipment;

    @ValidationMapping("$.tangible_assets.motor_vehicles.depreciation.other_adjustments")
    private Long motorVehicles;

    @ValidationMapping("$.tangible_assets.total.depreciation.other_adjustments")
    private Long total;
}
