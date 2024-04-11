package uk.gov.companieshouse.web.accounts.service.notehandler;

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
import uk.gov.companieshouse.api.model.common.ApiResource;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.MissingInfrastructureException;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NoteResourceHandlerFactoryTest {
    private NoteResourceHandlerFactory<ApiResource> noteResourceHandlerFactory;

    @Mock
    private NoteResourceHandler<ApiResource> noteResourceHandler;

    @BeforeEach
    public void setUp() {
        when(noteResourceHandler.getNoteType()).thenReturn(NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS);

        List<NoteResourceHandler<ApiResource>> noteResourceHandlers = new ArrayList<>();
        noteResourceHandlers.add(noteResourceHandler);

        noteResourceHandlerFactory = new NoteResourceHandlerFactory<>(noteResourceHandlers);
    }

    @Test
    @DisplayName("Get note transformer - success")
    void getNoteResourceHandlerSuccess() {
        NoteResourceHandler<ApiResource> returned =
                noteResourceHandlerFactory.getNoteResourceHandler(NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS);

        assertNotNull(returned);
        assertEquals(noteResourceHandler, returned);
    }

    @Test
    @DisplayName("Get note transformer - MissingInfrastructureException")
    void getNoteResourceHandlerMissingInfrastructureException() {
        assertThrows(MissingInfrastructureException.class, () ->
                noteResourceHandlerFactory.getNoteResourceHandler(NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS));
    }
}
