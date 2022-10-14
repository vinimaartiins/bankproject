package com.bank.credsystem.card;

import com.bank.credsystem.client.Client;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class CardFactory {
    private static final Random random = new Random();

    private CardFactory() {
    }

    public static Card makeCard(Client client, CardCreatorParams cardParams) {
        return Card.builder()
                .number(CardFactory.generateCardNumber(cardParams.getFlag()))
                .cardHolderName(CardFactory.generateHolderName(client.getName()))
                .expirationDate(CardFactory.generateExpirationDate())
                .cvv(CardFactory.generateRandomNumber(999, 0))
                .password(cardParams.getCardPassword())
                .cardLimit(generateBalance(client.getSalary()))
                .cardLimit(generateBalance(client.getSalary()))
                .cardFlag(cardParams.getFlag())
                .active(false)
                .owner(client)
                .build();
    }

    private static String generateCardNumber(CardFlag cardFlag) {
        return Objects.requireNonNull(cardFlag.getFlagNumber())
                .concat(CardFactory.generateRandomNumber(9999, 1000))
                .concat(CardFactory.generateRandomNumber(9999, 1000))
                .concat(CardFactory.generateRandomNumber(9999, 1000));
    }

    private static String generateHolderName(String name) {
        var index = 0;
        var lastIndex = Arrays.stream(name.split(" ")).count() - 1;
        var formattedName = new StringBuilder();
        for (var n : name.split(" ")) {
            if (index == 0) formattedName.append(n).append(" ");
            else if (index == lastIndex) formattedName.append(n);
            else formattedName.append(n.charAt(0)).append(" ");
            index++;
        }
        return formattedName.toString().toUpperCase(Locale.ROOT);
    }

    private static String generateExpirationDate() {
        var date = LocalDate.now();
        return String.valueOf(date.getMonthValue())
                .concat("/")
                .concat(String.valueOf(date.getYear() + 5));
    }

    private static Double generateBalance(double salary) {
        var percentage = 0.3;
        var balance = salary * percentage;
        if (balance < 300) return 300D;
        if (balance > 2000) return 2000D;
        return balance;
    }

    private static String generateRandomNumber(int upperBound, int lowerBound) {
        return String.valueOf(random.nextInt(upperBound - lowerBound) + lowerBound);
    }
}
