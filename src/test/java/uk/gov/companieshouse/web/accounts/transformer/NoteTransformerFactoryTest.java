package uk.gov.companieshouse.web.accounts.transformer;

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
import uk.gov.companieshouse.web.accounts.model.Note;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NoteTransformerFactoryTest {
    private NoteTransformerFactory<Note, ApiResource> noteTransformerFactory;

    @Mock
    private NoteTransformer<Note, ApiResource> noteTransformer;

    @BeforeEach
    public void setUp() {
        when(noteTransformer.getNoteType()).thenReturn(NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS);

        List<NoteTransformer<Note, ApiResource>> noteTransformers = new ArrayList<>();
        noteTransformers.add(noteTransformer);

        noteTransformerFactory = new NoteTransformerFactory<>(noteTransformers);
    }

    @Test
    @DisplayName("Get note transformer - success")
    void getNoteTransformerSuccess() {
        NoteTransformer<Note, ApiResource> returned =
                noteTransformerFactory.getNoteTransformer(NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS);

        assertNotNull(returned);
        assertEquals(noteTransformer, returned);
    }

    @Test
    @DisplayName("Get note transformer - MissingInfrastructureException")
    void getNoteTransformerMissingInfrastructureException() {
        assertThrows(MissingInfrastructureException.class, () ->
                noteTransformerFactory.getNoteTransformer(NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS));
    }
}
