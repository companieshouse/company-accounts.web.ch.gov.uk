package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorApi;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.transformer.DateTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorTransformer;

@Component
public class DirectorTransformerImpl implements DirectorTransformer {

    @Autowired
    private DateTransformer dateTransformer;

    private static final Pattern DIRECTOR_URI_PATTERN = Pattern.compile("/transactions/[^/]+/company-accounts/[^/]+/small-full/directors-report/directors/(.*)");

    @Override
    public DirectorApi getDirectorApi(DirectorToAdd director) {

        DirectorApi directorApi = new DirectorApi();
        directorApi.setName(director.getName());

        if (Boolean.TRUE.equals(director.getWasDirectorAppointedDuringPeriod())) {
            directorApi.setAppointmentDate(dateTransformer.toLocalDate(director.getAppointmentDate()));
        }

        if (Boolean.TRUE.equals(director.getDidDirectorResignDuringPeriod())) {
            directorApi.setResignationDate(dateTransformer.toLocalDate(director.getResignationDate()));
        }

        return directorApi;
    }

    @Override
    public Director[] getAllDirectors(DirectorApi[] directors) {

        Director[] allDirectors = new Director[directors.length];

        for (int i = 0; i < directors.length; i++) {

            Director director = new Director();
            director.setName(directors[i].getName());
            director.setAppointmentDate(directors[i].getAppointmentDate());
            director.setResignationDate(directors[i].getResignationDate());

            Matcher matcher = DIRECTOR_URI_PATTERN.matcher(directors[i].getLinks().getSelf());
            matcher.find();
            director.setId(matcher.group(1));

            allDirectors[i] = director;
        }

        return allDirectors;
    }
}
