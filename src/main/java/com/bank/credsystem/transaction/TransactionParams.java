package com.bank.credsystem.transaction;

import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class TransactionParams {
    @NotNull(message = "can not be null")
    private String cardPassword;
    @NotNull(message = "can not be null")
    private Long cardId;
    @Valid
    private TransactionDTO transaction;
}
