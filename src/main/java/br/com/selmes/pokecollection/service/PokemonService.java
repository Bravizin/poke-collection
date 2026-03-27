package br.com.selmes.pokecollection.service;

import br.com.selmes.pokecollection.domain.Pokemon;
import br.com.selmes.pokecollection.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonService {

    private final PokemonRepository repository;
    private final PokeApiService pokeApiService;

    public List<Pokemon> listarTodos() {
        return repository.findAll();
    }

    public Pokemon buscarPorPokedex(Integer pokedex) {
        return repository.findById(pokedex)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pokémon não encontrado para a pokedex " + pokedex
                ));
    }

    public List<Pokemon> buscarPorGeracao(Integer geracao) {
        return repository.findByGeracao(geracao);
    }

    public List<Pokemon> buscarPorPosse(boolean tenho) {
        return repository.findByTenho(tenho);
    }

    public List<Pokemon> buscarPorGeracaoEPosse(Integer geracao, boolean tenho) {
        return repository.findByGeracaoAndTenho(geracao, tenho);
    }

    public List<Pokemon> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    public Pokemon criarOuAtualizar(Pokemon pokemon) {
        return repository.save(pokemon);
    }

    public void deletar(Integer pokedex) {
        if (!repository.existsById(pokedex)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokémon não encontrado para a pokedex " + pokedex);
        }
        repository.deleteById(pokedex);
    }

    /**
     * Sincroniza um Pokémon da PokéAPI.
     * Se já existe no banco, atualiza apenas nome e geração (preserva o campo 'tenho').
     */
    public Pokemon sincronizarPokemon(Integer pokedexNumber) {
        log.info("Sincronizando Pokémon #{} da PokéAPI", pokedexNumber);

        // Buscar dados da API
        Pokemon dadosApi = pokeApiService.fetchPokemonData(pokedexNumber);

        // Verificar se já existe no banco
        Optional<Pokemon> existente = repository.findById(pokedexNumber);

        if (existente.isPresent()) {
            // Atualizar apenas dados fixos, preservando 'tenho'
            Pokemon pokemon = existente.get();
            pokemon.setNome(dadosApi.getNome());
            pokemon.setGeracao(dadosApi.getGeracao());
            return repository.save(pokemon);
        } else {
            // Criar novo registro (tenho = false por padrão)
            return repository.save(dadosApi);
        }
    }

    /**
     * Sincroniza um range de Pokémon (útil para popular o banco inicial)
     */
    public void sincronizarRange(Integer inicio, Integer fim) {
        log.info("Sincronizando Pokémon de {} a {}", inicio, fim);

        for (int i = inicio; i <= fim; i++) {
            try {
                sincronizarPokemon(i);
                Thread.sleep(100); // Evitar sobrecarga na API
            } catch (Exception e) {
                log.error("Erro ao sincronizar Pokémon #{}: {}", i, e.getMessage());
            }
        }

        log.info("Sincronização concluída!");
    }

    /**
     * Atualiza apenas o status de posse (tenho/não tenho)
     */
    public Pokemon atualizarPosse(Integer pokedex, boolean tenho) {
        Pokemon pokemon = buscarPorPokedex(pokedex);
        pokemon.setTenho(tenho);
        return repository.save(pokemon);
    }
}

