package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.transformer.DateTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorTransformer;

@Component
public class DirectorTransformerImpl implements DirectorTransformer {

    @Autowired
    private DateTransformer dateTransformer;

    @Override
    public DirectorApi getDirectorApi(DirectorToAdd director) {

        DirectorApi directorApi = new DirectorApi();
        directorApi.setName(director.getName());

        if (director.getWasDirectorAppointedDuringPeriod()) {
            directorApi.setAppointmentDate(dateTransformer.toLocalDate(director.getAppointmentDate()));
        }

        if (director.getDidDirectorResignDuringPeriod()) {
            directorApi.setResignationDate(dateTransformer.toLocalDate(director.getResignationDate()));
        }

        return directorApi;
    }
}
