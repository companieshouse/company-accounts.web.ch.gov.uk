package uk.gov.companieshouse.web.accounts.model.directorsreport;

import org.springframework.stereotype.Component;

@Component
public class AddOrRemoveDirectors {

    private Director[] existingDirectors;

    private DirectorToAdd directorToAdd;

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
