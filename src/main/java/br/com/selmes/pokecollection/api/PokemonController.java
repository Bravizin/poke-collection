package br.com.selmes.pokecollection.api;

import br.com.selmes.pokecollection.domain.Pokemon;
import br.com.selmes.pokecollection.service.PokemonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pokemons")
public class PokemonController {

    private final PokemonService service;

    public PokemonController(PokemonService service) {
        this.service = service;
    }

    /**
     * Lista todos os Pokémon
     */
    @GetMapping
    public List<Pokemon> listarTodos() {
        return service.listarTodos();
    }

    /**
     * Busca Pokémon por número da Pokédex
     */
    @GetMapping("/{pokedex}")
    public Pokemon buscarPorPokedex(@PathVariable Integer pokedex) {
        return service.buscarPorPokedex(pokedex);
    }

    /**
     * Busca Pokémon por geração
     * Exemplo: GET /api/pokemons/geracao/1
     */
    @GetMapping("/geracao/{geracao}")
    public List<Pokemon> buscarPorGeracao(@PathVariable Integer geracao) {
        return service.buscarPorGeracao(geracao);
    }

    /**
     * Busca Pokémon que você possui ou não possui
     * Exemplo: GET /api/pokemons/posse?tenho=true
     */
    @GetMapping("/posse")
    public List<Pokemon> buscarPorPosse(@RequestParam boolean tenho) {
        return service.buscarPorPosse(tenho);
    }

    /**
     * Busca Pokémon por geração e status de posse
     * Exemplo: GET /api/pokemons/geracao/1/posse?tenho=true
     */
    @GetMapping("/geracao/{geracao}/posse")
    public List<Pokemon> buscarPorGeracaoEPosse(
            @PathVariable Integer geracao,
            @RequestParam boolean tenho) {
        return service.buscarPorGeracaoEPosse(geracao, tenho);
    }

    /**
     * Busca Pokémon por nome (parcial, case insensitive)
     * Exemplo: GET /api/pokemons/buscar?nome=pika
     */
    @GetMapping("/buscar")
    public List<Pokemon> buscarPorNome(@RequestParam String nome) {
        return service.buscarPorNome(nome);
    }

    /**
     * Atualiza apenas o status de posse (tenho/não tenho)
     * Exemplo: PATCH /api/pokemons/25/posse?tenho=true
     */
    @PatchMapping("/{pokedex}/posse")
    public ResponseEntity<Pokemon> atualizarPosse(
            @PathVariable Integer pokedex,
            @RequestParam boolean tenho) {

        Pokemon pokemon = service.atualizarPosse(pokedex, tenho);
        return ResponseEntity.ok(pokemon);
    }
}

