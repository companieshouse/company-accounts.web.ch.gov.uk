package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl;

import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsColumns;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsDepreciation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsNetBookValue;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.TangibleAssetsResourceTransformer;

public class TangibleAssetsOfficeEquipmentTransformerImpl extends TangibleAssetsResourceTransformerImpl implements TangibleAssetsResourceTransformer {

    @Override
    public void mapTangibleAssetsResourceToWebModel(TangibleAssets tangibleAssets,
                                                    TangibleAssetsResource tangibleAssetsResource) {

        if (tangibleAssetsResource.getCost() != null) {

            TangibleAssetsCost tangibleAssetsCost = createCost(tangibleAssets);

            TangibleAssetsColumns atPeriodStart = createCostAtPeriodStart(tangibleAssetsCost);
            atPeriodStart.setOfficeEquipment(tangibleAssetsResource.getCost().getAtPeriodStart());

            TangibleAssetsColumns additions = createAdditions(tangibleAssetsCost);
            additions.setOfficeEquipment(tangibleAssetsResource.getCost().getAdditions());

            TangibleAssetsColumns disposals = createDisposals(tangibleAssetsCost);
            disposals.setOfficeEquipment(tangibleAssetsResource.getCost().getDisposals());

            TangibleAssetsColumns revaluations = createRevaluations(tangibleAssetsCost);
            revaluations.setOfficeEquipment(tangibleAssetsResource.getCost().getRevaluations());

            TangibleAssetsColumns transfers = createTransfers(tangibleAssetsCost);
            transfers.setOfficeEquipment(tangibleAssetsResource.getCost().getTransfers());

            TangibleAssetsColumns atPeriodEnd = createCostAtPeriodEnd(tangibleAssetsCost);
            atPeriodEnd.setOfficeEquipment(tangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (tangibleAssetsResource.getDepreciation() != null) {

            TangibleAssetsDepreciation tangibleAssetsDepreciation = createDepreciation(tangibleAssets);

            TangibleAssetsColumns atPeriodStart = createDepreciationAtPeriodStart(tangibleAssetsDepreciation);
            atPeriodStart.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getAtPeriodStart());

            TangibleAssetsColumns chargeForYear = createChargeForYear(tangibleAssetsDepreciation);
            chargeForYear.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getChargeForYear());

            TangibleAssetsColumns onDisposals = createOnDisposals(tangibleAssetsDepreciation);
            onDisposals.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getOnDisposals());

            TangibleAssetsColumns otherAdjustments = createOtherAdjustments(tangibleAssetsDepreciation);
            otherAdjustments.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getOtherAdjustments());

            TangibleAssetsColumns atPeriodEnd = createDepreciationAtPeriodEnd(tangibleAssetsDepreciation);
            atPeriodEnd.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getAtPeriodEnd());
        }

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = createNetBookValue(tangibleAssets);

        TangibleAssetsColumns currentPeriod = createCurrentPeriod(tangibleAssetsNetBookValue);
        currentPeriod.setOfficeEquipment(tangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        TangibleAssetsColumns previousPeriod = createPreviousPeriod(tangibleAssetsNetBookValue);
        previousPeriod.setOfficeEquipment(tangibleAssetsResource.getNetBookValueAtEndOfPreviousPeriod());
    }
}
