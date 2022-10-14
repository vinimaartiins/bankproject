package com.bank.credsystem.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query(value = "SELECT * FROM tb_client c WHERE c.cpf = :cpf", nativeQuery = true)
    Optional<Client> findByCpf(@Param("cpf") String cpf);
}
