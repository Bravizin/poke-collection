# Poke Collection API - Documentação para Frontend

## Informações Gerais

- **Base URL**: `http://localhost:8080`
- **Autenticação**: Não requerida
- **Content-Type**: `application/json`

## Modelo de Dados

### Pokemon
```json
{
  "pokedex": 25,
  "nome": "Pikachu",
  "geracao": 1,
  "tenho": false
}
```

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `pokedex` | Integer | Número da Pokédex (ID único) |
| `nome` | String | Nome do Pokémon |
| `geracao` | Integer | Geração do Pokémon (1-9) |
| `tenho` | Boolean | Indica se você possui o Pokémon |

## Endpoints

### 1. Listar Todos os Pokémon
```http
GET /api/pokemons
```

**Resposta**: Array de 1025 Pokémon
```json
[
  {
    "pokedex": 1,
    "nome": "Bulbasaur",
    "geracao": 1,
    "tenho": false
  },
  {
    "pokedex": 25,
    "nome": "Pikachu",
    "geracao": 1,
    "tenho": true
  }
]
```

---

### 2. Buscar Pokémon por Número da Pokédex
```http
GET /api/pokemons/{pokedex}
```

**Exemplo**: `GET /api/pokemons/25`

**Resposta**:
```json
{
  "pokedex": 25,
  "nome": "Pikachu",
  "geracao": 1,
  "tenho": false
}
```

**Erros**:
- `404 Not Found`: Pokémon não encontrado

---

### 3. Buscar por Geração
```http
GET /api/pokemons/geracao/{geracao}
```

**Exemplo**: `GET /api/pokemons/geracao/1`

**Gerações Disponíveis**:
- Geração 1: #1-151 (Kanto)
- Geração 2: #152-251 (Johto)
- Geração 3: #252-386 (Hoenn)
- Geração 4: #387-493 (Sinnoh)
- Geração 5: #494-649 (Unova)
- Geração 6: #650-721 (Kalos)
- Geração 7: #722-809 (Alola)
- Geração 8: #810-905 (Galar)
- Geração 9: #906-1025 (Paldea)

**Resposta**: Array de Pokémon da geração especificada

---

### 4. Buscar por Status de Posse
```http
GET /api/pokemons/posse?tenho={true|false}
```

**Exemplos**:
- `GET /api/pokemons/posse?tenho=true` - Pokémon que você possui
- `GET /api/pokemons/posse?tenho=false` - Pokémon que você não possui

**Resposta**: Array de Pokémon filtrados

---

### 5. Buscar por Geração e Posse
```http
GET /api/pokemons/geracao/{geracao}/posse?tenho={true|false}
```

**Exemplo**: `GET /api/pokemons/geracao/1/posse?tenho=true`

**Uso**: Ver quais Pokémon da Geração 1 você possui

**Resposta**: Array de Pokémon filtrados

---

### 6. Buscar por Nome
```http
GET /api/pokemons/buscar?nome={termo}
```

**Exemplos**:
- `GET /api/pokemons/buscar?nome=pika` - Retorna Pikachu e variações
- `GET /api/pokemons/buscar?nome=char` - Retorna Charmander, Charmeleon, Charizard

**Resposta**: Array de Pokémon que contêm o termo no nome (case insensitive)

---

### 7. Atualizar Status de Posse
```http
PATCH /api/pokemons/{pokedex}/posse?tenho={true|false}
```

**Exemplos**:
- `PATCH /api/pokemons/25/posse?tenho=true` - Marcar que possui Pikachu
- `PATCH /api/pokemons/150/posse?tenho=false` - Marcar que não possui Mewtwo

**Resposta**:
```json
{
  "pokedex": 25,
  "nome": "Pikachu",
  "geracao": 1,
  "tenho": true
}
```

**Erros**:
- `404 Not Found`: Pokémon não encontrado

---

## Exemplos de Uso no Frontend

### Fetch API (JavaScript)

```javascript
// Listar todos
const response = await fetch('http://localhost:8080/api/pokemons');
const pokemons = await response.json();

// Buscar por número
const pikachu = await fetch('http://localhost:8080/api/pokemons/25')
  .then(res => res.json());

// Buscar por geração
const geracao1 = await fetch('http://localhost:8080/api/pokemons/geracao/1')
  .then(res => res.json());

// Buscar os que você possui
const meusPokemon = await fetch('http://localhost:8080/api/pokemons/posse?tenho=true')
  .then(res => res.json());

// Buscar por nome
const resultados = await fetch('http://localhost:8080/api/pokemons/buscar?nome=pika')
  .then(res => res.json());

// Marcar como "tenho"
await fetch('http://localhost:8080/api/pokemons/25/posse?tenho=true', {
  method: 'PATCH'
});

// Marcar como "não tenho"
await fetch('http://localhost:8080/api/pokemons/25/posse?tenho=false', {
  method: 'PATCH'
});
```

### Axios (JavaScript)

```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/pokemons'
});

// Listar todos
const { data } = await api.get('/');

// Buscar por número
const { data: pikachu } = await api.get('/25');

// Buscar por geração
const { data: geracao1 } = await api.get('/geracao/1');

// Buscar os que você possui
const { data: meusPokemon } = await api.get('/posse', { params: { tenho: true } });

// Buscar por nome
const { data: resultados } = await api.get('/buscar', { params: { nome: 'pika' } });

// Atualizar posse
await api.patch('/25/posse', null, { params: { tenho: true } });
```

---

## Funcionalidades Sugeridas para o Frontend

### 1. Lista Completa
- Grid/lista com todos os 1025 Pokémon
- Checkbox ou toggle para marcar "tenho/não tenho"
- Filtro por geração
- Busca por nome
- Contador: "X/1025 capturados"

### 2. Visualização por Geração
- Tabs ou dropdown para selecionar geração
- Progresso por geração: "45/151 Geração 1"
- Barra de progresso visual

### 3. Minha Coleção
- Visualizar apenas os Pokémon que você possui
- Opção de ver os que faltam

### 4. Card do Pokémon
- Número da Pokédex
- Nome
- Geração
- Indicador visual de posse (cor, ícone, etc)
- Botão para alternar status

### 5. Estatísticas
- Total capturados
- Porcentagem de conclusão
- Progresso por geração
- Pokémon mais recentes adicionados

---

## Observações Importantes

1. **Apenas Leitura + Alteração de Posse**: A API não permite criar, editar dados fixos ou deletar Pokémon. Apenas consultar e alterar o campo `tenho`.

2. **Dados Fixos**: Os dados (nome, número, geração) estão fixos no banco e sincronizados com a PokéAPI.

3. **Performance**: Endpoint `/api/pokemons` retorna 1025 registros. Considere:
   - Paginação no frontend
   - Carregar sob demanda
   - Cache local

4. **CORS**: Se o frontend rodar em outra porta, pode ser necessário configurar CORS no backend.

---

## Estrutura de Pastas Backend (Referência)

```
src/main/java/br/com/selmes/pokecollection/
├── api/
│   └── PokemonController.java      # Endpoints REST
├── domain/
│   └── Pokemon.java                # Entidade JPA
├── service/
│   ├── PokemonService.java         # Lógica de negócio
│   └── PokeApiService.java         # Integração com PokéAPI
├── repository/
│   └── PokemonRepository.java      # Acesso ao banco
└── config/
    ├── SecurityConfig.java         # Configuração de segurança
    └── RestTemplateConfig.java     # RestTemplate bean
```

---

## Tecnologias do Backend

- **Spring Boot 4.0.5**
- **Java 21**
- **PostgreSQL**
- **Spring Data JPA**
- **Lombok**

---

## Contato e Suporte

Para dúvidas sobre os endpoints ou estrutura dos dados, consulte:
- Código fonte: `PokemonController.java`
- Modelo: `Pokemon.java`
- Testes via Postman incluídos no repositório
