package uk.gov.companieshouse.web.accounts.temp;

import java.util.EnumMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.common.ApiResource;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;

@Component
public class NoteHelperFactory<A extends ApiResource> {

    private EnumMap<NoteType, NoteHelper<A>> noteHelperMap;

    @Autowired
    public NoteHelperFactory(List<NoteHelper<A>> noteHelpers) {

        this.noteHelperMap = new EnumMap<>(NoteType.class);

        noteHelpers.forEach(noteHelper -> noteHelperMap.put(noteHelper.getNoteType(), noteHelper));
    }

    public NoteHelper<A> getNoteHelper(NoteType noteType) {

        NoteHelper<A> noteHelper = noteHelperMap.get(noteType);
        if (noteHelper == null) {
            throw new RuntimeException();
        }
        return noteHelper;
    }
}
