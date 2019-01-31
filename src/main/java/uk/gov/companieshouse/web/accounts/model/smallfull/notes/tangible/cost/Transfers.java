package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class Transfers {

    @ValidationMapping("$.tangible_assets.land_and_buildings.cost.transfers")
    private Long landAndBuildings;

    @ValidationMapping("$.tangible_assets.plant_and_machinery.cost.transfers")
    private Long plantAndMachinery;

    @ValidationMapping("$.tangible_assets.fixtures_and_fittings.cost.transfers")
    private Long fixturesAndFittings;

    @ValidationMapping("$.tangible_assets.office_equipment.cost.transfers")
    private Long officeEquipment;

    @ValidationMapping("$.tangible_assets.motor_vehicles.cost.transfers")
    private Long motorVehicles;

    @ValidationMapping("$.tangible_assets.total.cost.transfers")
    private Long total;
}
