package br.com.selmes.pokecollection.bootstrap;

import br.com.selmes.pokecollection.repository.PokemonRepository;
import br.com.selmes.pokecollection.service.PokemonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PokemonDataLoader implements CommandLineRunner {

    private final PokemonService pokemonService;
    private final PokemonRepository pokemonRepository;

    // Total de Pokémon por geração (até a geração 9)
    private static final int[] POKEMON_POR_GERACAO = {
            151,  // Geração 1: 1-151
            251,  // Geração 2: 152-251
            386,  // Geração 3: 252-386
            493,  // Geração 4: 387-493
            649,  // Geração 5: 494-649
            721,  // Geração 6: 650-721
            809,  // Geração 7: 722-809
            905,  // Geração 8: 810-905
            1025  // Geração 9: 906-1025
    };

    @Override
    public void run(String... args) {
        long count = pokemonRepository.count();

        if (count >= 1025) {
            log.info("✓ Base de dados já está populada com {} Pokémon. Pulando sincronização.", count);
            return;
        }

        log.info("=================================================");
        log.info("Base de dados vazia ou incompleta ({}/1025 Pokémon)", count);
        log.info("Iniciando sincronização de dados da PokéAPI...");
        log.info("ATENÇÃO: Este processo pode levar ~2-3 minutos");
        log.info("=================================================");

        // Sincronizar todas as 9 gerações
        int inicio = 1;
        for (int geracao = 1; geracao <= 9; geracao++) {
            int fim = POKEMON_POR_GERACAO[geracao - 1];

            log.info("Sincronizando Geração {}: Pokémon #{} a #{}", geracao, inicio, fim);

            try {
                pokemonService.sincronizarRange(inicio, fim);
                log.info("✓ Geração {} sincronizada com sucesso!", geracao);
            } catch (Exception e) {
                log.error("✗ Erro ao sincronizar Geração {}: {}", geracao, e.getMessage());
            }

            inicio = fim + 1;
        }

        log.info("=================================================");
        log.info("✓ Sincronização concluída! Total: 1025 Pokémon");
        log.info("=================================================");
    }
}
