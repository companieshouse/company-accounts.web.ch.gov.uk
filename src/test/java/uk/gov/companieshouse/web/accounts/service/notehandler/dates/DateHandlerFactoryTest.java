package uk.gov.companieshouse.web.accounts.service.notehandler.dates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.MissingInfrastructureException;
import uk.gov.companieshouse.web.accounts.model.Note;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DateHandlerFactoryTest {

    private DateHandlerFactory<Note> dateHandlerFactory;

    @Mock
    private DateHandler<Note> dateHandler;

    @BeforeEach
    public void setup() {

        when(dateHandler.getNoteType()).thenReturn(
                NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS);

        List<DateHandler<Note>> dateHandlers = new ArrayList<>();
        dateHandlers.add(dateHandler);

        dateHandlerFactory = new DateHandlerFactory<>(dateHandlers);
    }

    @Test
    @DisplayName("Get date handler - success")
    void getDateHandlerSuccess() {

        DateHandler<Note> returned =
                dateHandlerFactory.getDateHandler(
                        NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS);

        assertNotNull(returned);
        assertEquals(dateHandler, returned);
    }

    @Test
    @DisplayName("Get date handler - MissingInfrastructureException")
    void getDateHandlerMissingInfrastructureException() {

        assertThrows(MissingInfrastructureException.class, () ->
                dateHandlerFactory.getDateHandler(NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS));
    }
}
