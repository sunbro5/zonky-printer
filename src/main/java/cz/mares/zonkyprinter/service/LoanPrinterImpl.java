package cz.mares.zonkyprinter.service;

import cz.mares.zonkyprinter.model.Loan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoanPrinterImpl implements LoansPrinter {

    @Override
    public void print(Loan loan) {
        log.info(loan.toString());
    }
}
