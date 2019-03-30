package cz.mares.zonkyprinter.scheduler;

import cz.mares.zonkyprinter.model.Loan;
import cz.mares.zonkyprinter.service.LoansPrinter;
import cz.mares.zonkyprinter.service.ZonkyLoansLoader;
import cz.mares.zonkyprinter.service.filter.LoanRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@Slf4j
public class ZonkyPrintScheduledJob {

    private static final long FIVE_MINUTES = 300000L;

    private volatile LocalDateTime lastRunDate;

    @Autowired
    private LoansPrinter loansPrinter;

    @Autowired
    private ZonkyLoansLoader loansLoader;

    //@Scheduled(fixedDelay = FIVE_MINUTES)// TODO vypsat pouze behem 5 minut publikovane nebo vsechny ???
    public void printNewLoans() {
        log.info("printNewLoans started: " + LocalDateTime.now().toString());
        LocalDateTime beforeFiveMinutes = getLastRunDate();
        LoanRequestFilter requestFilter = LoanRequestFilter.builder()
                .publishedDate(Date.from(beforeFiveMinutes.atZone(ZoneId.systemDefault()).toInstant()))
                .build();
        List<Loan> loanList = loansLoader.getLoans(requestFilter);
        loanList.forEach(loansPrinter::print);
    }

    @Scheduled(fixedDelay = FIVE_MINUTES)
    public void printAllLoans() {
        log.info("printAllLoans started: " + LocalDateTime.now().toString());
        List<Loan> loanList = loansLoader.getLoans();
        loanList.forEach(loansPrinter::print);
    }

    private synchronized LocalDateTime getLastRunDate() {
        if (lastRunDate == null) {
            lastRunDate = LocalDateTime.now().minusMinutes(5);
        }
        return lastRunDate;
    }
}
