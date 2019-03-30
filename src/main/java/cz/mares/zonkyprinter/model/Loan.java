package cz.mares.zonkyprinter.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Loan {
    private int id;
    private String url;
    private String name;
    private String story;
    private String purpose;
    private List<LoanPhoto> photos;
    private String nickname;
    private int termInMonths;
    private double interesRate;
    private double revenueRate;
    private int annuityWithInsurance;
    private String rating;
    private boolean topped;
    private BigDecimal amount;
    private BigDecimal remainingInvestment;
    private BigDecimal reservedAmount;
    private Double investmentRate;
    private boolean covered;
    private Date datePublished;
    private boolean published;
    private Date deadline;
    private int investmentsCount;
    private int questionsCount;
    private String region;
    private String mainIncomeType;
    private boolean insuranceActive;
    private List<LoanInsuranceHistory> insuranceHistory;

}
