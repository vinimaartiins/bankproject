package com.bank.credsystem.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardCreatorParams {
    @NotNull(message = "Card ID can not be null.")
    private Long clientId;
    @NotNull(message = "Card password can not be null.")
    private String cardPassword;
    @NotNull(message = "Flag can not be null.")
    private CardFlag flag;
}
