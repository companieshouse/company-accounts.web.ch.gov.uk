package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class Disposals {

    @ValidationMapping("$.tangible_assets.land_and_buildings.cost.disposals")
    private Long landAndBuildings;

    @ValidationMapping("$.tangible_assets.plant_and_machinery.cost.disposals")
    private Long plantAndMachinery;

    @ValidationMapping("$.tangible_assets.fixtures_and_fittings.cost.disposals")
    private Long fixturesAndFittings;

    @ValidationMapping("$.tangible_assets.office_equipment.cost.disposals")
    private Long officeEquipment;

    @ValidationMapping("$.tangible_assets.motor_vehicles.cost.disposals")
    private Long motorVehicles;

    @ValidationMapping("$.tangible_assets.total.cost.disposals")
    private Long total;
}
