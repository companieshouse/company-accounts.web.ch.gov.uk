package uk.gov.companieshouse.web.accounts.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;

public class DateTransformerTest {

    private DateTransformer dateTransformer = new DateTransformer();

    private static final String DAY = "8";

    private static final String MONTH = "12";

    private static final String YEAR = "2019";

    private static final LocalDate EXPECTED_DATE = LocalDate.of(2019, 12, 8);

    @Test
    @DisplayName("Date to LocalDate")
    void toLocalDate() {

        Date date = new Date();
        date.setDay(DAY);
        date.setMonth(MONTH);
        date.setYear(YEAR);

        assertEquals(EXPECTED_DATE, dateTransformer.toLocalDate(date));
    }
}