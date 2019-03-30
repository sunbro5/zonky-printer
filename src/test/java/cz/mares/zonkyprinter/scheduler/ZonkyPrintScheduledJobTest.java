package cz.mares.zonkyprinter.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.mares.zonkyprinter.model.Loan;
import cz.mares.zonkyprinter.service.LoansPrinter;
import cz.mares.zonkyprinter.service.ZonkyLoansLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@RestClientTest({ZonkyLoansLoader.class, ZonkyPrintScheduledJob.class})
public class ZonkyPrintScheduledJobTest {

    @Autowired
    private ZonkyPrintScheduledJob zonkyPrintScheduledJob;

    @MockBean
    private LoansPrinter loansPrinter;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void printOneLoan() throws JsonProcessingException {

        Loan loan = new Loan();
        loan.setAmount(BigDecimal.TEN);
        loan.setId(1);
        Loan[] loans = Collections.singletonList(loan).toArray(new Loan[1]);
        mockRestTemplateResponse(loans, "https://api.zonky.cz/loans/marketplace");

        zonkyPrintScheduledJob.printAllLoans();

        verify(loansPrinter).print(any());
    }

    @Test
    public void printOneLoanGivenDateFilter() throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();

        ReflectionTestUtils.setField(zonkyPrintScheduledJob, "lastRunDate", now);

        Loan loan = new Loan();
        loan.setAmount(BigDecimal.TEN);
        loan.setId(1);
        Loan[] loans = Collections.singletonList(loan).toArray(new Loan[1]);

        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String dateNow = now.format(simpleDateFormat);

        mockRestTemplateResponse(loans, "https://api.zonky.cz/loans/marketplace?datePublished__gt=" + dateNow);

        zonkyPrintScheduledJob.printNewLoans();

        verify(loansPrinter).print(any());
    }

    @Test
    public void printEmptyLoans() throws JsonProcessingException {
        mockRestTemplateResponse(null, "https://api.zonky.cz/loans/marketplace");

        zonkyPrintScheduledJob.printNewLoans();

        verifyZeroInteractions(loansPrinter);
    }

    private void mockRestTemplateResponse(Loan[] loans, String requestUrl) throws JsonProcessingException {
        String detailsString =
                objectMapper.writeValueAsString(loans);

        this.server.expect(requestTo(requestUrl))
                .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));
    }
}