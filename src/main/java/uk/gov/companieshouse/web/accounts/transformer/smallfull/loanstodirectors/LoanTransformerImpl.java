package uk.gov.companieshouse.web.accounts.transformer.smallfull.loanstodirectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoanApi;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Loan;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LoanTransformerImpl implements LoanTransformer {

    @Autowired
    BreakdownTransformer breakdownTransformer;

    private static final Pattern LOAN_URI_PATTERN = Pattern.compile("/transactions/.+?/company-accounts/.+?/small-full/notes/loans-to-directors/loans/(.*)");

    @Override
    public LoanApi getLoanApi(LoanToAdd loanToAdd) {

        LoanApi loanApi = new LoanApi();

        loanApi.setDirectorName(loanToAdd.getDirectorName());

        loanApi.setDescription(loanToAdd.getDescription());

        loanApi.setBreakdown(breakdownTransformer.mapLoanBreakdownToApi(loanToAdd));

        return loanApi;
    }

    @Override
    public Loan[] getAllLoans(LoanApi[] loans) {

        Loan[] allLoans = new Loan[loans.length];

        for (int i = 0; i < loans.length; i++) {

            Loan loan = new Loan();
            loan.setDirectorName(loans[i].getDirectorName());
            loan.setDescription(loans[i].getDescription());
            loan.setBreakdown(breakdownTransformer.mapLoanBreakdownToWeb(loans[i]));

            Matcher matcher = LOAN_URI_PATTERN.matcher(loans[i].getLinks().getSelf());
            matcher.find();
            loan.setId(matcher.group(1));

            allLoans[i] = loan;
        }

        return allLoans;
    }
}
