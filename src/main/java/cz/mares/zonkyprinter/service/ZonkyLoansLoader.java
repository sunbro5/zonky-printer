package cz.mares.zonkyprinter.service;

import cz.mares.zonkyprinter.model.Loan;
import cz.mares.zonkyprinter.service.filter.LoanRequestFilter;

import java.util.List;

public interface ZonkyLoansLoader {

    List<Loan> getLoans();

    List<Loan> getLoans(LoanRequestFilter filter);
}
