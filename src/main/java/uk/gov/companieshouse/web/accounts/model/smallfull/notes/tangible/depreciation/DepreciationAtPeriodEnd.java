package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class DepreciationAtPeriodEnd {

    @ValidationMapping("$.tangible_assets.land_and_buildings.depreciation.at_period_end")
    private Long landAndBuildings;

    @ValidationMapping("$.tangible_assets.plant_and_machinery.depreciation.at_period_end")
    private Long plantAndMachinery;

    @ValidationMapping("$.tangible_assets.fixtures_and_fittings.depreciation.at_period_end")
    private Long fixturesAndFittings;

    @ValidationMapping("$.tangible_assets.office_equipment.depreciation.at_period_end")
    private Long officeEquipment;

    @ValidationMapping("$.tangible_assets.motor_vehicles.depreciation.at_period_end")
    private Long motorVehicles;

    @ValidationMapping("$.tangible_assets.total.depreciation.at_period_end")
    private Long total;

    public Long getLandAndBuildings() {
        return landAndBuildings;
    }

    public void setLandAndBuildings(Long landAndBuildings) {
        this.landAndBuildings = landAndBuildings;
    }

    public Long getPlantAndMachinery() {
        return plantAndMachinery;
    }

    public void setPlantAndMachinery(Long plantAndMachinery) {
        this.plantAndMachinery = plantAndMachinery;
    }

    public Long getFixturesAndFittings() {
        return fixturesAndFittings;
    }

    public void setFixturesAndFittings(Long fixturesAndFittings) {
        this.fixturesAndFittings = fixturesAndFittings;
    }

    public Long getOfficeEquipment() {
        return officeEquipment;
    }

    public void setOfficeEquipment(Long officeEquipment) {
        this.officeEquipment = officeEquipment;
    }

    public Long getMotorVehicles() {
        return motorVehicles;
    }

    public void setMotorVehicles(Long motorVehicles) {
        this.motorVehicles = motorVehicles;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
