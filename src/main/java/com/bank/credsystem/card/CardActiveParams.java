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
public class CardActiveParams {
    @NotNull(message = "Can not be null.")
    private Long cardId;
    @NotNull(message = "Can not be null.")
    private boolean enableCard;
}
