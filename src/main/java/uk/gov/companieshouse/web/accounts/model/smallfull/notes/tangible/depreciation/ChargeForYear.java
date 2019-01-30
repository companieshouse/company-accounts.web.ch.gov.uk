package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class ChargeForYear {

    @ValidationMapping("$.tangible_assets.land_and_buildings.depreciation.charge_for_year")
    private Long landAndBuildings;

    @ValidationMapping("$.tangible_assets.plant_and_machinery.depreciation.charge_for_year")
    private Long plantAndMachinery;

    @ValidationMapping("$.tangible_assets.fixtures_and_fittings.depreciation.charge_for_year")
    private Long fixturesAndFittings;

    @ValidationMapping("$.tangible_assets.office_equipment.depreciation.charge_for_year")
    private Long officeEquipment;

    @ValidationMapping("$.tangible_assets.motor_vehicles.depreciation.charge_for_year")
    private Long motorVehicles;

    @ValidationMapping("$.tangible_assets.total.depreciation.charge_for_year")
    private Long total;
}
