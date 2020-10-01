package uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorApi;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorLinks;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.transformer.DateTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorTransformerImplTest {

    @Mock
    private DateTransformer dateTransformer;

    @InjectMocks
    private DirectorTransformer directorTransformer = new DirectorTransformerImpl();

    private static final String NAME = "name";

    private static final LocalDate APPOINTMENT_DATE = LocalDate.of(2019, 1, 1);

    private static final LocalDate RESIGNATION_DATE = LocalDate.of(2019, 12, 31);

    private static final String DIRECTOR_ID = "directorId";

    private static final String DIRECTOR_SELF_LINK =
            "/transactions/transactionId/company-accounts/companyAccountsId/small-full/directors-report/directors/" + DIRECTOR_ID;

    @Test
    @DisplayName("Get director API - no dates")
    void getDirectorApiNoDates() {

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setName(NAME);
        directorToAdd.setWasDirectorAppointedDuringPeriod(false);
        directorToAdd.setDidDirectorResignDuringPeriod(false);

        DirectorApi directorApi = directorTransformer.getDirectorApi(directorToAdd);

        assertNotNull(directorApi);
        assertEquals(NAME, directorApi.getName());
        assertNull(directorApi.getAppointmentDate());
        assertNull(directorApi.getResignationDate());
    }

    @Test
    @DisplayName("Get director API - has appointment date")
    void getDirectorApiHasAppointmentDate() {

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setName(NAME);
        directorToAdd.setWasDirectorAppointedDuringPeriod(true);
        directorToAdd.setDidDirectorResignDuringPeriod(false);

        when(dateTransformer.toLocalDate(directorToAdd.getAppointmentDate())).thenReturn(APPOINTMENT_DATE);

        DirectorApi directorApi = directorTransformer.getDirectorApi(directorToAdd);

        assertNotNull(directorApi);
        assertEquals(NAME, directorApi.getName());
        assertEquals(APPOINTMENT_DATE, directorApi.getAppointmentDate());
        assertNull(directorApi.getResignationDate());
    }

    @Test
    @DisplayName("Get director API - has resignation date")
    void getDirectorApiHasResignationDate() {

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setName(NAME);
        directorToAdd.setWasDirectorAppointedDuringPeriod(false);
        directorToAdd.setDidDirectorResignDuringPeriod(true);

        when(dateTransformer.toLocalDate(directorToAdd.getResignationDate())).thenReturn(RESIGNATION_DATE);

        DirectorApi directorApi = directorTransformer.getDirectorApi(directorToAdd);

        assertNotNull(directorApi);
        assertEquals(NAME, directorApi.getName());
        assertNull(directorApi.getAppointmentDate());
        assertEquals(RESIGNATION_DATE, directorApi.getResignationDate());
    }

    @Test
    @DisplayName("Get all directors")
    void getAllDirectors() {

        DirectorApi directorApi = new DirectorApi();
        directorApi.setName(NAME);
        directorApi.setAppointmentDate(APPOINTMENT_DATE);
        directorApi.setResignationDate(RESIGNATION_DATE);

        DirectorLinks directorLinks = new DirectorLinks();
        directorLinks.setSelf(DIRECTOR_SELF_LINK);

        directorApi.setLinks(directorLinks);

        Director[] allDirectors = directorTransformer.getAllDirectors(new DirectorApi[]{directorApi});

        assertEquals(1, allDirectors.length);
        assertEquals(NAME, allDirectors[0].getName());
        assertEquals(APPOINTMENT_DATE, allDirectors[0].getAppointmentDate());
        assertEquals(RESIGNATION_DATE, allDirectors[0].getResignationDate());
        assertEquals(DIRECTOR_ID, allDirectors[0].getId());
    }
}
