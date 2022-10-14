package com.bank.credsystem.card;

import com.bank.credsystem.client.Client;
import com.bank.credsystem.transaction.Transaction;
import com.bank.credsystem.util.AESEncryptor;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Convert(converter = AESEncryptor.class)
    private String number;
    @Convert(converter = AESEncryptor.class)
    private String cardHolderName;
    @Convert(converter = AESEncryptor.class)
    private String expirationDate;
    @Convert(converter = AESEncryptor.class)
    private String cvv;
    @Convert(converter = AESEncryptor.class)
    private String password;
    private double cardLimit;
    private CardFlag cardFlag;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private Client owner;
    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Transaction> transaction;
}
