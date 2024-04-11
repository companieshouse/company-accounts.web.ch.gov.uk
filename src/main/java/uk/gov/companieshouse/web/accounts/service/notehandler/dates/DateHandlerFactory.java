package uk.gov.companieshouse.web.accounts.service.notehandler.dates;

import java.util.EnumMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.MissingInfrastructureException;
import uk.gov.companieshouse.web.accounts.model.Note;

@Component
public class DateHandlerFactory<N extends Note> {
    private EnumMap<NoteType, DateHandler<N>> dateHandlerMap;

    @Autowired
    public DateHandlerFactory(List<DateHandler<N>> dateHandlers) {
        this.dateHandlerMap = new EnumMap<>(NoteType.class);

        dateHandlers.forEach(
                dateHandler -> dateHandlerMap.put(dateHandler.getNoteType(),
                        dateHandler));
    }

    public DateHandler<N> getDateHandler(NoteType noteType) {
        DateHandler<N> dateHandler = dateHandlerMap.get(noteType);
        if (dateHandler == null) {
            throw new MissingInfrastructureException("No date handler found for note type: " + noteType.toString());
        }
        return dateHandler;
    }
}
