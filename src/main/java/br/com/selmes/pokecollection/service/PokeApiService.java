package br.com.selmes.pokecollection.service;

import br.com.selmes.pokecollection.domain.Pokemon;
import br.com.selmes.pokecollection.dto.PokeApiPokemonResponse;
import br.com.selmes.pokecollection.dto.PokeApiSpeciesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokeApiService {

    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2";
    private final RestTemplate restTemplate;

    /**
     * Busca dados de um Pokémon específico da PokéAPI
     */
    public Pokemon fetchPokemonData(Integer pokedexNumber) {
        try {
            // Buscar dados básicos
            String pokemonUrl = POKEAPI_BASE_URL + "/pokemon/" + pokedexNumber;
            PokeApiPokemonResponse pokemonResponse = restTemplate.getForObject(pokemonUrl, PokeApiPokemonResponse.class);

            // Buscar dados de espécie (geração)
            String speciesUrl = POKEAPI_BASE_URL + "/pokemon-species/" + pokedexNumber;
            PokeApiSpeciesResponse speciesResponse = restTemplate.getForObject(speciesUrl, PokeApiSpeciesResponse.class);

            if (pokemonResponse == null || speciesResponse == null) {
                throw new RuntimeException("Pokémon não encontrado na PokéAPI");
            }

            return Pokemon.builder()
                    .pokedex(pokemonResponse.getId())
                    .nome(capitalize(pokemonResponse.getName()))
                    .geracao(speciesResponse.getGenerationNumber())
                    .tenho(false) // Padrão: não possui
                    .build();

        } catch (Exception e) {
            log.error("Erro ao buscar Pokémon #{} da PokéAPI: {}", pokedexNumber, e.getMessage());
            throw new RuntimeException("Erro ao buscar dados do Pokémon: " + e.getMessage());
        }
    }

    /**
     * Sincroniza dados de múltiplos Pokémon (útil para popular o banco inicialmente)
     */
    public void syncPokemonRange(Integer start, Integer end) {
        log.info("Sincronizando Pokémon de {} a {}", start, end);
        for (int i = start; i <= end; i++) {
            try {
                fetchPokemonData(i);
                log.debug("Pokémon #{} sincronizado", i);

                // Delay para não sobrecarregar a API
                Thread.sleep(100);
            } catch (Exception e) {
                log.warn("Falha ao sincronizar Pokémon #{}: {}", i, e.getMessage());
            }
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
