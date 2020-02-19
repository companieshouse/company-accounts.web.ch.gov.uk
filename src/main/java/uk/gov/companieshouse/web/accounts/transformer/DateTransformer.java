package uk.gov.companieshouse.web.accounts.transformer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;

@Component
public class DateTransformer {

    public LocalDate toLocalDate(Date date) {

        return LocalDate.parse(date.getYear() + "-" + date.getMonth() + "-" + date.getDay(),
                DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT));
    }
}
