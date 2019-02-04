package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.netbookvalue;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class PreviousPeriod {

    @ValidationMapping("$.tangible_assets.land_and_buildings.net_book_value_at_end_of_previous_period")
    private Long landAndBuildings;

    @ValidationMapping("$.tangible_assets.plant_and_machinery.net_book_value_at_end_of_previous_period")
    private Long plantAndMachinery;

    @ValidationMapping("$.tangible_assets.fixtures_and_fittings.net_book_value_at_end_of_previous_period")
    private Long fixturesAndFittings;

    @ValidationMapping("$.tangible_assets.office_equipment.net_book_value_at_end_of_previous_period")
    private Long officeEquipment;

    @ValidationMapping("$.tangible_assets.motor_vehicles.net_book_value_at_end_of_previous_period")
    private Long motorVehicles;

    @ValidationMapping("$.tangible_assets.total.net_book_value_at_end_of_previous_period")
    private Long total;
}
