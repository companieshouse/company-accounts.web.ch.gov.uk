package uk.gov.companieshouse.web.accounts.model.directorsreport;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class AddOrRemoveDirectors {

    private Director[] existingDirectors;

    private DirectorToAdd directorToAdd;

    @ValidationMapping("$.secretary.name")
    private String secretary;

    public Director[] getExistingDirectors() {
        return existingDirectors;
    }

    public void setExistingDirectors(Director[] existingDirectors) {
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
