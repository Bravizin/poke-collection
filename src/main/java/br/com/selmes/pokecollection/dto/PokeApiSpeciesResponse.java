package br.com.selmes.pokecollection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeApiSpeciesResponse {

    @JsonProperty("generation")
    private Generation generation;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Generation {
        private String name; // "generation-i", "generation-ii", etc.
        private String url;
    }

    public Integer getGenerationNumber() {
        if (generation != null && generation.getName() != null) {
            String genName = generation.getName();
            String romanNumeral = genName.replace("generation-", "");
            return romanToInt(romanNumeral);
        }
        return 1;
    }

    private Integer romanToInt(String roman) {
        return switch (roman.toLowerCase()) {
            case "i" -> 1;
            case "ii" -> 2;
            case "iii" -> 3;
            case "iv" -> 4;
            case "v" -> 5;
            case "vi" -> 6;
            case "vii" -> 7;
            case "viii" -> 8;
            case "ix" -> 9;
            default -> 1;
        };
    }
}
