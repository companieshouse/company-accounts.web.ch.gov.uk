package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class Revaluations {

    @ValidationMapping("$.tangible_assets.land_and_buildings.cost.revaluations")
    private Long landAndBuildings;

    @ValidationMapping("$.tangible_assets.plant_and_machinery.cost.revaluations")
    private Long plantAndMachinery;

    @ValidationMapping("$.tangible_assets.fixtures_and_fittings.cost.revaluations")
    private Long fixturesAndFittings;

    @ValidationMapping("$.tangible_assets.office_equipment.cost.revaluations")
    private Long officeEquipment;

    @ValidationMapping("$.tangible_assets.motor_vehicles.cost.revaluations")
    private Long motorVehicles;

    @ValidationMapping("$.tangible_assets.total.cost.revaluations")
    private Long total;
}
