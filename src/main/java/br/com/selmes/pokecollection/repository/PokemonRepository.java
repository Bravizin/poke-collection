package br.com.selmes.pokecollection.repository;

import br.com.selmes.pokecollection.domain.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {

    /**
     * Busca Pokémon por geração
     */
    List<Pokemon> findByGeracao(Integer geracao);

    /**
     * Busca Pokémon que você possui
     */
    List<Pokemon> findByTenho(boolean tenho);

    /**
     * Busca Pokémon por geração e status de posse
     */
    List<Pokemon> findByGeracaoAndTenho(Integer geracao, boolean tenho);

    /**
     * Busca Pokémon por nome (case insensitive)
     */
    List<Pokemon> findByNomeContainingIgnoreCase(String nome);
}

