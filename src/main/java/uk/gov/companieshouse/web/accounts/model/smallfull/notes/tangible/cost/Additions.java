package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

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
