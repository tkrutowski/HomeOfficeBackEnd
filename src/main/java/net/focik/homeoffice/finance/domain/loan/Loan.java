package net.focik.homeoffice.finance.domain.loan;

import lombok.*;
import net.focik.homeoffice.addresses.domain.Address;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Loan {

    private int id;
    private int idBank;
    private int idUser;
    private String name;
    private BigDecimal amount;
    private LocalDate date;
    private String loanNumber;
    private String accountNumber;
    private LocalDate firstPaymentDate;
    private Integer numberOfInstallments;
    private BigDecimal installmentAmount;
    private LoanStatus loanStatus;
    private BigDecimal loanCost;//prowizja itp
    private String otherInfo;
    private List<LoanInstallment> loanInstallments;

    public void addLoanInstallment(List<LoanInstallment> loanInstallments) {
        this.loanInstallments = loanInstallments;
    }

    public void changeLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

}