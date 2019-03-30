package cz.mares.zonkyprinter.model;

import lombok.Data;

import java.util.Date;

@Data
public class LoanInsuranceHistory {

    private Date policyPeriodFrom;
    private Date policyPeriodTo;

}
