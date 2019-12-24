package uk.gov.companieshouse.web.accounts.model.directorsreport;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Component
@ValidationModel
public class AddOrRemoveDirectors {

    private Director[] existingDirectors;

    private DirectorToAdd directorToAdd;

    private String secretary;

    public Director[] getExistingDirectors() {
        return existingDirectors;
    }

    @ValidationMapping("validation.length.minInvalid.directors.director_available")
    public boolean directorIsAvailable;


    public boolean isDirectorIsAvailable() {
        return directorIsAvailable;
    }

    public void setDirectorIsAvailable(boolean directorIsAvailable) {
        this.directorIsAvailable = directorIsAvailable;
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
