package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class CostAtPeriodEnd {

    @ValidationMapping("$.tangible_assets.land_and_buildings.cost.at_period_end")
    private Long landAndBuildings;

    @ValidationMapping("$.tangible_assets.plant_and_machinery.cost.at_period_end")
    private Long plantAndMachinery;

    @ValidationMapping("$.tangible_assets.fixtures_and_fittings.cost.at_period_end")
    private Long fixturesAndFittings;

    @ValidationMapping("$.tangible_assets.office_equipment.cost.at_period_end")
    private Long officeEquipment;

    @ValidationMapping("$.tangible_assets.motor_vehicles.cost.at_period_end")
    private Long motorVehicles;

    @ValidationMapping("$.tangible_assets.total.cost.at_period_end")
    private Long total;
}
