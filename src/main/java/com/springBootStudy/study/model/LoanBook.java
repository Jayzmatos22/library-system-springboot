package com.springBootStudy.study.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.time.LocalDate;

@Entity
public class LoanBook {

    // data empréstimo x data prevista de devolução
    private LocalDate returnDate;
    private LocalDate dueDate; // prazo previsto (+7)
    private LocalDate loanDate;

    @Transient
    public boolean isLate() {
        if (returnDate != null) {
            return returnDate.isAfter(dueDate);
        } // voltou depois do prazo?
        return LocalDate.now().isAfter(dueDate); // a data atual passa da prevista pra devolução?
    }
}
