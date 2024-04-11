package uk.gov.companieshouse.web.accounts.transformer;

import java.util.EnumMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.common.ApiResource;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.MissingInfrastructureException;
import uk.gov.companieshouse.web.accounts.model.Note;

@Component
public class NoteTransformerFactory<W extends Note, A extends ApiResource> {
    private EnumMap<NoteType, NoteTransformer<W, A>> noteTransformerMap;

    @Autowired
    public NoteTransformerFactory(List<NoteTransformer<W, A>> noteTransformers) {
        this.noteTransformerMap = new EnumMap<>(NoteType.class);

        noteTransformers.forEach(noteTransformer -> noteTransformerMap.put(noteTransformer.getNoteType(), noteTransformer));
    }

    public NoteTransformer<W, A> getNoteTransformer(NoteType noteType) {
        NoteTransformer<W, A> noteTransformer = noteTransformerMap.get(noteType);
        if (noteTransformer == null) {
            throw new MissingInfrastructureException("No note transformer found for note type: " + noteType.toString());
        }
        return noteTransformer;
    }
}
