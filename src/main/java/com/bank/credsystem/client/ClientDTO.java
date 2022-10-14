package com.bank.credsystem.client;

import com.bank.credsystem.card.CardDTO;
import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private Long id;
    @Pattern(regexp = "\\d{11}", message = "Must have 11 digits.")
    @NotNull(message = "Can not be null")
    private String cpf;
    @NotNull(message = "Can not be null.")
    @NotBlank(message = "Can not be blank.")
    private String name;
    @Pattern(regexp = "\\d{11}", message = "Must have 11 digits.")
    private String contactNumber;
    @NotNull(message = "Can not be null.")
    @NotBlank(message = "Can not be blank.")
    private String address;
    @DecimalMin(value = "0.0", message = "Can not be less than 0.")
    private Double salary;
    private List<CardDTO> cardList;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.cpf = client.getCpf();
        this.name = client.getName();
        this.contactNumber = client.getContactNumber();
        this.address = client.getAddress();
        this.salary = client.getSalary();
        this.cardList = client.getCard() == null ? new ArrayList<>() : client.getCard().stream().map(CardDTO::new).collect(Collectors.toList());
    }
}
