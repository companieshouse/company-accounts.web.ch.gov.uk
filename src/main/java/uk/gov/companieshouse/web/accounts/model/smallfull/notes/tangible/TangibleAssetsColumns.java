package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssetsColumns {

    @JsonProperty("land_and_buildings")
    private Long landAndBuildings;

    @JsonProperty("plant_and_machinery")
    private Long plantAndMachinery;

    @JsonProperty("fixtures_and_fittings")
    private Long fixturesAndFittings;

    @JsonProperty("office_equipment")
    private Long officeEquipment;

    @JsonProperty("motor_vehicles")
    private Long motorVehicles;

    @JsonProperty("total")
    private Long total;

}
