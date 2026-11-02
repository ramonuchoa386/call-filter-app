# Guia de Desenvolvimento & Convenções

Este documento estabelece as diretrizes arquiteturais, padrões de código e princípios de desenvolvimento para qualquer engenheiro ou agente de IA atuando no repositório **Call Filter**.

---

## Principais Diretrizes Arquiteturais

1. **Arquitetura Hexagonal Estrita:**
   - O pacote `domain/` **NÃO DEVE** importar qualquer classe pertencente ao SDK do Android (`android.*`) ou de frameworks externos.
   - O domínio interage com o mundo externo exclusivamente através de **Interfaces/Ports de Saída** (`domain/port/output`).
   - Todos os pontos de entrada do sistema Android (Activities, Services, BroadcastReceivers) devem ser posicionados dentro de `adapter/input/`.

2. **Desenvolvimento Orientado a Testes (TDD):**
   - Todas as novas regras de negócio ou alterações em casos de uso devem ser precedidas por testes unitários na pasta `app/src/test/java/com/example/callfilter/domain/`.
   - Utilize mocks ou implementações *Fake* de repositórios/portas para testar o domínio de forma isolada e síncrona.

3. **Simplicidade & Evitar Super-Engenharia (KISS / YAGNI):**
   - Não crie abstrações desnecessárias, métodos auxiliares sem uso imediato ou frameworks customizados dentro do app.
   - Dê preferência aos componentes nativos do Android e Kotlin padrão antes de adicionar novas dependências de terceiros no `build.gradle.kts`.

---

## Convenções de Código Kotlin

- **Nomes de Testes:** Escreva os nomes das funções de teste utilizando texto descritivo entre crases em português. Exemplo:
    ```kotlin
    @Test
    fun `deve bloquear numero especifico quando estiver na lista de excecao`() { ... }
    ```

* **Imutabilidade:** Priorize o uso de `val` e data classes imutáveis para representação de modelos de domínio (`CallSettings`).
* **Gerenciamento de Dependências:** Adicione novas bibliotecas somente quando estritamente necessário no arquivo `app/build.gradle.kts`.

---

## Fluxo para Adição de Novas Funcionalidades (Backlog)

1. Identificar se a nova funcionalidade impacta o Domínio ou apenas a Camada de Adaptadores.
2. Criar ou atualizar os testes unitários do Domínio.
3. Implementar a regra no Domínio (`domain/model`, `domain/port`).
4. Criar ou atualizar o adaptador correspondente (`adapter/input` ou `adapter/output`).
5. Validar a aplicação executando `./gradlew test` e `./gradlew assembleDebug`.