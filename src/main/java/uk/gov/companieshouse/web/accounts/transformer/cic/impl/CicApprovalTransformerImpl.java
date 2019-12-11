package uk.gov.companieshouse.web.accounts.transformer.cic.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.cic.approval.CicApprovalApi;
import uk.gov.companieshouse.web.accounts.model.cic.CicApproval;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicApprovalTransformer;

@Component
public class CicApprovalTransformerImpl implements CicApprovalTransformer {

    @Override
    public CicApprovalApi getCicApprovalApi(CicApproval cicApproval) {
        CicApprovalApi cicApprovalApi = new CicApprovalApi();
        cicApprovalApi.setDate(getCicApprovalDate(cicApproval));
        cicApprovalApi.setName(cicApproval.getDirectorName());
        return cicApprovalApi;
    }

    @Override
    public LocalDate getCicApprovalDate(CicApproval cicApproval) {

        return LocalDate.parse(
            cicApproval.getDate().getYear() + "-" +
                cicApproval.getDate().getMonth() + "-" +
                cicApproval.getDate().getDay(),
            DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT));
    }

    @Override
    public CicApproval getCicApproval(CicApprovalApi cicApprovalApi) {

        CicApproval cicApproval = new CicApproval();
        Date approvalDate = new Date();
        LocalDate apiDate = cicApprovalApi.getDate();

        approvalDate.setDay(Integer.toString(apiDate.getDayOfMonth()));
        approvalDate.setMonth(Integer.toString(apiDate.getMonthValue()));
        approvalDate.setYear(Integer.toString(apiDate.getYear()));

        cicApproval.setDate(approvalDate);

        cicApproval.setDirectorName(cicApprovalApi.getName());

        return cicApproval;
    }
}
