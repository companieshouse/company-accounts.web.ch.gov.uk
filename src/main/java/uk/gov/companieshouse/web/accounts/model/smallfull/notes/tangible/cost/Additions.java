package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class Additions {

    @ValidationMapping("$.tangible_assets.land_and_buildings.cost.additions")
    private Long landAndBuildings;

    @ValidationMapping("$.tangible_assets.plant_and_machinery.cost.additions")
    private Long plantAndMachinery;

    @ValidationMapping("$.tangible_assets.fixtures_and_fittings.cost.additions")
    private Long fixturesAndFittings;

    @ValidationMapping("$.tangible_assets.office_equipment.cost.additions")
    private Long officeEquipment;

    @ValidationMapping("$.tangible_assets.motor_vehicles.cost.additions")
    private Long motorVehicles;

    @ValidationMapping("$.tangible_assets.total.cost.additions")
    private Long total;
}