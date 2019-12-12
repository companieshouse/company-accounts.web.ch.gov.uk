package uk.gov.companieshouse.web.accounts.transformer.cic.impl;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.cic.approval.CicApprovalApi;
import uk.gov.companieshouse.web.accounts.model.cic.CicApproval;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;
import uk.gov.companieshouse.web.accounts.transformer.DateTransformer;
import uk.gov.companieshouse.web.accounts.transformer.cic.CicApprovalTransformer;

@Component
public class CicApprovalTransformerImpl implements CicApprovalTransformer {

    @Autowired
    private DateTransformer dateTransformer;

    @Override
    public CicApprovalApi getCicApprovalApi(CicApproval cicApproval) {
        CicApprovalApi cicApprovalApi = new CicApprovalApi();
        cicApprovalApi.setDate(dateTransformer.toLocalDate(cicApproval.getDate()));
        cicApprovalApi.setName(cicApproval.getDirectorName());
        return cicApprovalApi;
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
