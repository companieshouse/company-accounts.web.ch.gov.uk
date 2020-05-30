package uk.gov.companieshouse.web.accounts.service.notehandler;

import java.util.EnumMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.common.ApiResource;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.MissingInfrastructureException;

@Component
public class NoteResourceHandlerFactory<A extends ApiResource> {

    private EnumMap<NoteType, NoteResourceHandler<A>> noteHelperMap;

    @Autowired
    public NoteResourceHandlerFactory(List<NoteResourceHandler<A>> noteResourceHandlers) {

        this.noteHelperMap = new EnumMap<>(NoteType.class);

        noteResourceHandlers.forEach(
                noteResourceHandler -> noteHelperMap.put(noteResourceHandler.getNoteType(),
                        noteResourceHandler));
    }

    public NoteResourceHandler<A> getNoteResourceHandler(NoteType noteType) {

        NoteResourceHandler<A> noteResourceHandler = noteHelperMap.get(noteType);
        if (noteResourceHandler == null) {
            throw new MissingInfrastructureException("No note resource handler found for note type: " + noteType.toString());
        }
        return noteResourceHandler;
    }
}
