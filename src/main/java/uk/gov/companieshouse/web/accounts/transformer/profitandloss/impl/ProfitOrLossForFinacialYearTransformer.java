package uk.gov.companieshouse.web.accounts.transformer.profitandloss.impl;

import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitAndLossApi;
import uk.gov.companieshouse.web.accounts.model.profitandloss.ProfitAndLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.items.Tax;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.items.TotalProfitOrLossForFinancialYear;

@Component
public class ProfitOrLossForFinacialYearTransformer {

    public void addCurrentPeriodToWebModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi currentPeriodProfitAndLoss) {

        if (currentPeriodProfitAndLoss.getProfitOrLossForFinancialYear() != null) {

            createProfitOrLossForFinancialYear(profitAndLoss);

            if (currentPeriodProfitAndLoss.getProfitOrLossForFinancialYear().getTax() != null) {

                Tax tax = createTax(profitAndLoss);
                tax.setCurrentAmount(
                        currentPeriodProfitAndLoss.getProfitOrLossForFinancialYear().getTax());
            }

            if (currentPeriodProfitAndLoss.getProfitOrLossForFinancialYear().getTotalProfitOrLossForFinancialYear() != null) {

                TotalProfitOrLossForFinancialYear total = createTotalProfitOrLossForFinancialYear(profitAndLoss);
                total.setCurrentAmount(
                        currentPeriodProfitAndLoss.getProfitOrLossForFinancialYear().getTotalProfitOrLossForFinancialYear());
            }
        }
    }

    public void addPreviousPeriodToWebModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi previousPeriodProfitAndLoss) {

        if (previousPeriodProfitAndLoss.getProfitOrLossForFinancialYear() != null) {

            createProfitOrLossForFinancialYear(profitAndLoss);

            if (previousPeriodProfitAndLoss.getProfitOrLossForFinancialYear().getTax() != null) {

                Tax tax = createTax(profitAndLoss);
                tax.setPreviousAmount(
                        previousPeriodProfitAndLoss.getProfitOrLossForFinancialYear().getTax());
            }

            if (previousPeriodProfitAndLoss.getProfitOrLossForFinancialYear().getTotalProfitOrLossForFinancialYear() != null) {

                TotalProfitOrLossForFinancialYear total = createTotalProfitOrLossForFinancialYear(profitAndLoss);
                total.setPreviousAmount(
                        previousPeriodProfitAndLoss.getProfitOrLossForFinancialYear().getTotalProfitOrLossForFinancialYear());
            }
        }
    }

    public void addCurrentPeriodToApiModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi currentPeriodProfitAndLoss) {

        if (hasCurrentPeriodProfitOrLossForFinancialYear(profitAndLoss)) {

            uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitOrLossForFinancialYear profitOrLossForFinancialYear =
                    new uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitOrLossForFinancialYear();

            profitOrLossForFinancialYear.setTax(
                    profitAndLoss.getProfitOrLossForFinancialYear().getTax().getCurrentAmount());
            profitOrLossForFinancialYear.setTotalProfitOrLossForFinancialYear(
                    profitAndLoss.getProfitOrLossForFinancialYear().getTotalProfitOrLossForFinancialYear().getCurrentAmount());

            currentPeriodProfitAndLoss.setProfitOrLossForFinancialYear(profitOrLossForFinancialYear);
        }
    }

    public void addPreviousPeriodToApiModel(ProfitAndLoss profitAndLoss, ProfitAndLossApi previousPeriodProfitAndLoss) {

        if (hasPreviousPeriodProfitOrLossForFinancialYear(profitAndLoss)) {

            uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitOrLossForFinancialYear profitOrLossForFinancialYear =
                    new uk.gov.companieshouse.api.model.accounts.profitandloss.ProfitOrLossForFinancialYear();

            profitOrLossForFinancialYear.setTax(
                    profitAndLoss.getProfitOrLossForFinancialYear().getTax().getPreviousAmount());
            profitOrLossForFinancialYear.setTotalProfitOrLossForFinancialYear(
                    profitAndLoss.getProfitOrLossForFinancialYear().getTotalProfitOrLossForFinancialYear().getPreviousAmount());

            previousPeriodProfitAndLoss.setProfitOrLossForFinancialYear(profitOrLossForFinancialYear);
        }
    }

    private void createProfitOrLossForFinancialYear(ProfitAndLoss profitAndLoss) {

        if (profitAndLoss.getProfitOrLossForFinancialYear() == null) {

            profitAndLoss.setProfitOrLossForFinancialYear(new ProfitOrLossForFinancialYear());
        }
    }

    private Tax createTax(ProfitAndLoss profitAndLoss) {

        Tax tax;

        if (profitAndLoss.getProfitOrLossForFinancialYear().getTax() == null) {
            tax = new Tax();
            profitAndLoss.getProfitOrLossForFinancialYear().setTax(tax);
        } else {
            tax = profitAndLoss.getProfitOrLossForFinancialYear().getTax();
        }

        return tax;
    }

    private TotalProfitOrLossForFinancialYear createTotalProfitOrLossForFinancialYear(ProfitAndLoss profitAndLoss) {

        TotalProfitOrLossForFinancialYear total;

        if (profitAndLoss.getProfitOrLossForFinancialYear().getTotalProfitOrLossForFinancialYear() == null) {
            total = new TotalProfitOrLossForFinancialYear();
            profitAndLoss.getProfitOrLossForFinancialYear().setTotalProfitOrLossForFinancialYear(total);
        } else {
            total = profitAndLoss.getProfitOrLossForFinancialYear().getTotalProfitOrLossForFinancialYear();
        }

        return total;
    }

    private boolean hasCurrentPeriodProfitOrLossForFinancialYear(ProfitAndLoss profitAndLoss) {

        return Stream.of(profitAndLoss.getProfitOrLossForFinancialYear().getTax().getCurrentAmount(),
                         profitAndLoss.getProfitOrLossForFinancialYear().getTotalProfitOrLossForFinancialYear().getCurrentAmount()).
                anyMatch(Objects::nonNull);
    }

    private boolean hasPreviousPeriodProfitOrLossForFinancialYear(ProfitAndLoss profitAndLoss) {

        return Stream.of(profitAndLoss.getProfitOrLossForFinancialYear().getTax().getPreviousAmount(),
                         profitAndLoss.getProfitOrLossForFinancialYear().getTotalProfitOrLossForFinancialYear().getPreviousAmount()).
                anyMatch(Objects::nonNull);
    }
}
