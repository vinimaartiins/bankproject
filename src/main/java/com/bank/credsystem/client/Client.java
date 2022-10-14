package com.bank.credsystem.client;

import com.bank.credsystem.card.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tb_client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;
    private String name;
    private String contactNumber;
    private String address;
    private Double salary;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Card> card;

    public Client(ClientDTO clientDTO) {
        this.cpf = clientDTO.getCpf();
        this.name = clientDTO.getName();
        this.contactNumber = clientDTO.getContactNumber();
        this.address = clientDTO.getAddress();
        this.salary = clientDTO.getSalary();
    }
}
