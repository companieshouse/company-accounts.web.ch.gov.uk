package uk.gov.companieshouse.web.accounts.model.directorsreport;

import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorApi;

public class AddOrRemoveDirectors {

    private DirectorApi[] existingDirectors;

    private DirectorToAdd directorToAdd;

    private String secretary;

    public DirectorApi[] getExistingDirectors() {
        return existingDirectors;
    }

    public void setExistingDirectors(DirectorApi[] existingDirectors) {
        this.existingDirectors = existingDirectors;
    }

    public DirectorToAdd getDirectorToAdd() {
        return directorToAdd;
    }

    public void setDirectorToAdd(
            DirectorToAdd directorToAdd) {
        this.directorToAdd = directorToAdd;
    }

    public String getSecretary() {
        return secretary;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }
}
