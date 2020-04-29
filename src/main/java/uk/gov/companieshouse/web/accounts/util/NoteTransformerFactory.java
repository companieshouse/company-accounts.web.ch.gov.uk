package uk.gov.companieshouse.web.accounts.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.web.accounts.service.smallfull.impl.NoteTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NoteTransformerFactory {

    private Map<String, NoteTransformer> noteTransformerMap = new HashMap<>();

    @Autowired
    public NoteTransformerFactory(List<NoteTransformer> noteTransformers) {

        noteTransformers.forEach(transformer -> noteTransformerMap.put(transformer.getNoteType(), transformer));
    }

    public NoteTransformer getTransformer(String noteType) {

            return noteTransformerMap.get(noteType);
    }
}
