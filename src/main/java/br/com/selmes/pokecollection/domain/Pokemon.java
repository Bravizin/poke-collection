package br.com.selmes.pokecollection.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pokemon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pokemon {

    /**
     * Número da Pokédex, usado como ID.
     */
    @Id
    @Column(name = "pokedex", nullable = false, unique = true)
    private Integer pokedex;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "geracao", nullable = false)
    private Integer geracao;

    @Column(name = "tenho", nullable = false)
    private boolean tenho;
}

