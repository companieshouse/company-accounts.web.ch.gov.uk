package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.GreaterThanOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.OtherDebtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.PrepaymentsAndAccruedIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Total;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.TradeDebtors;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class DebtorsTransformerImpl implements NoteTransformer<Debtors, DebtorsApi> {
    @Override
    public Debtors toWeb(DebtorsApi debtorsApi) {
        Debtors debtors = new Debtors();

        if (debtorsApi == null) {
            return debtors;
        }

        populateCurrentPeriodForWeb(debtorsApi, debtors);
        populatePreviousPeriodForWeb(debtorsApi, debtors);

        return debtors;
    }

    private void populatePreviousPeriodForWeb(DebtorsApi debtorsApi, Debtors debtors) {
        PreviousPeriod previousPeriod = debtorsApi.getDebtorsPreviousPeriod();

        if (previousPeriod != null) {
            if (previousPeriod.getTradeDebtors() != null) {
                TradeDebtors tradeDebtors = createTradeDebtors(debtors);
                tradeDebtors.setPreviousTradeDebtors(previousPeriod.getTradeDebtors());
            }

            if (previousPeriod.getPrepaymentsAndAccruedIncome() != null) {
                PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome =
                        createPrepaymentsAndAccruedIncome(debtors);
                prepaymentsAndAccruedIncome.setPreviousPrepaymentsAndAccruedIncome(
                        previousPeriod.getPrepaymentsAndAccruedIncome());
            }

            if (previousPeriod.getOtherDebtors() != null) {
                OtherDebtors otherDebtors = createOtherDebtors(debtors);
                otherDebtors.setPreviousOtherDebtors(previousPeriod.getOtherDebtors());
            }

            if (previousPeriod.getTotal() != null) {
                Total total = createTotal(debtors);
                total.setPreviousTotal(previousPeriod.getTotal());
            }

            if (previousPeriod.getGreaterThanOneYear() != null) {
                GreaterThanOneYear greaterThanOneYear = createGreaterThanOneYear(debtors);
                greaterThanOneYear.setPreviousGreaterThanOneYear(previousPeriod.getGreaterThanOneYear());
            }
        }
    }

    private void populateCurrentPeriodForWeb(DebtorsApi debtorsApi, Debtors debtors) {
        CurrentPeriod currentPeriod = debtorsApi.getDebtorsCurrentPeriod();

        if (currentPeriod != null) {
            debtors.setDetails(currentPeriod.getDetails());

            if (currentPeriod.getTradeDebtors() != null) {
                TradeDebtors tradeDebtors = createTradeDebtors(debtors);
                tradeDebtors.setCurrentTradeDebtors(currentPeriod.getTradeDebtors());
            }

            if (currentPeriod.getPrepaymentsAndAccruedIncome() != null) {
                PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome =
                        createPrepaymentsAndAccruedIncome(debtors);
                prepaymentsAndAccruedIncome.setCurrentPrepaymentsAndAccruedIncome(
                        currentPeriod.getPrepaymentsAndAccruedIncome());
            }

            if (currentPeriod.getOtherDebtors() != null) {
                OtherDebtors otherDebtors = createOtherDebtors(debtors);
                otherDebtors.setCurrentOtherDebtors(currentPeriod.getOtherDebtors());
            }

            if (currentPeriod.getTotal() != null) {
                Total total = createTotal(debtors);
                total.setCurrentTotal(currentPeriod.getTotal());
            }

            if (currentPeriod.getGreaterThanOneYear() != null) {
                GreaterThanOneYear greaterThanOneYear = createGreaterThanOneYear(debtors);
                greaterThanOneYear.setCurrentGreaterThanOneYear(currentPeriod.getGreaterThanOneYear());
            }
        }
    }

    private TradeDebtors createTradeDebtors(Debtors debtors) {
        TradeDebtors tradeDebtors;

        if (debtors.getTradeDebtors() != null) {
            tradeDebtors = debtors.getTradeDebtors();
        } else {
            tradeDebtors = new TradeDebtors();
            debtors.setTradeDebtors(tradeDebtors);
        }

        return tradeDebtors;
    }

    private PrepaymentsAndAccruedIncome createPrepaymentsAndAccruedIncome(Debtors debtors) {
        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome;

        if (debtors.getPrepaymentsAndAccruedIncome() != null) {
            prepaymentsAndAccruedIncome = debtors.getPrepaymentsAndAccruedIncome();
        } else {
            prepaymentsAndAccruedIncome = new PrepaymentsAndAccruedIncome();
            debtors.setPrepaymentsAndAccruedIncome(prepaymentsAndAccruedIncome);
        }

        return prepaymentsAndAccruedIncome;
    }

    private OtherDebtors createOtherDebtors(Debtors debtors) {
        OtherDebtors otherDebtors;

        if (debtors.getOtherDebtors() != null) {
            otherDebtors = debtors.getOtherDebtors();
        } else {
            otherDebtors = new OtherDebtors();
            debtors.setOtherDebtors(otherDebtors);
        }

        return otherDebtors;
    }

    private Total createTotal(Debtors debtors) {
        Total total;

        if (debtors.getTotal() != null) {
            total = debtors.getTotal();
        } else {
            total = new Total();
            debtors.setTotal(total);
        }

        return total;
    }

    private GreaterThanOneYear createGreaterThanOneYear(Debtors debtors) {
        GreaterThanOneYear greaterThanOneYear;

        if (debtors.getGreaterThanOneYear() != null) {
            greaterThanOneYear = debtors.getGreaterThanOneYear();
        } else {
            greaterThanOneYear = new GreaterThanOneYear();
            debtors.setGreaterThanOneYear(greaterThanOneYear);
        }

        return greaterThanOneYear;
    }

    @Override
    public DebtorsApi toApi(Debtors debtors) {
        DebtorsApi debtorsApi = new DebtorsApi();

        setCurrentPeriodDebtorsOnApiModel(debtors, debtorsApi);
        setPreviousPeriodDebtorsOnApiModel(debtors, debtorsApi);

        return debtorsApi;
    }

    private void setPreviousPeriodDebtorsOnApiModel(Debtors debtors, DebtorsApi debtorsApi) {
        PreviousPeriod previousPeriod = new PreviousPeriod();

        previousPeriod.setTradeDebtors(debtors.getTradeDebtors().getPreviousTradeDebtors());

        previousPeriod.setPrepaymentsAndAccruedIncome(debtors.getPrepaymentsAndAccruedIncome().getPreviousPrepaymentsAndAccruedIncome());

        previousPeriod.setOtherDebtors(debtors.getOtherDebtors().getPreviousOtherDebtors());

        previousPeriod.setTotal(debtors.getTotal().getPreviousTotal());

        previousPeriod.setGreaterThanOneYear(debtors.getGreaterThanOneYear().getPreviousGreaterThanOneYear());

        if (isPreviousPeriodPopulated(previousPeriod)) {
            debtorsApi.setDebtorsPreviousPeriod(previousPeriod);
        }
    }

    private void setCurrentPeriodDebtorsOnApiModel(Debtors debtors, DebtorsApi debtorsApi) {
        CurrentPeriod currentPeriod = new CurrentPeriod();

        if (StringUtils.isNotBlank(debtors.getDetails())) {
            currentPeriod.setDetails(debtors.getDetails());
        }

        currentPeriod.setTradeDebtors(debtors.getTradeDebtors().getCurrentTradeDebtors());

        currentPeriod.setPrepaymentsAndAccruedIncome(debtors.getPrepaymentsAndAccruedIncome().getCurrentPrepaymentsAndAccruedIncome());

        currentPeriod.setOtherDebtors(debtors.getOtherDebtors().getCurrentOtherDebtors());

        currentPeriod.setTotal(debtors.getTotal().getCurrentTotal());

        currentPeriod.setGreaterThanOneYear(debtors.getGreaterThanOneYear().getCurrentGreaterThanOneYear());

        if (isCurrentPeriodPopulated(currentPeriod)) {
            debtorsApi.setDebtorsCurrentPeriod(currentPeriod);
        }
    }

    private boolean isCurrentPeriodPopulated(CurrentPeriod currentPeriod) {
        return Stream.of(currentPeriod.getDetails(),
                currentPeriod.getGreaterThanOneYear(),
                currentPeriod.getOtherDebtors(),
                currentPeriod.getPrepaymentsAndAccruedIncome(),
                currentPeriod.getTotal(),
                currentPeriod.getTradeDebtors()).anyMatch(Objects::nonNull);
    }

    private boolean isPreviousPeriodPopulated(PreviousPeriod previousPeriod) {
        return Stream.of(previousPeriod.getGreaterThanOneYear(),
                previousPeriod.getOtherDebtors(),
                previousPeriod.getPrepaymentsAndAccruedIncome(),
                previousPeriod.getTotal(),
                previousPeriod.getTradeDebtors()).anyMatch(Objects::nonNull);
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_DEBTORS;
    }
}
