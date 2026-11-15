# ADR 003: Migração Total para Jetpack Compose e Adição de Gerenciamento de Estado Reativo com StateFlow

* **Status:** Aceito
* **Data:** 2026-11-09
* **Contexto:** Épico 2 - Call History and Block Management (Projeto Call Filter)

---

## Contexto e Problema

Com a conclusão da fundação técnica no Épico 1 (Room, Hilt e migração de dados), o aplicativo necessitava implementar as telas de auditoria de chamadas (`HistoryScreen`) e gerenciamento de contatos bloqueados (`BlockedContactsScreen`), além da tela de configurações e da navegação principal. 

A abordagem tradicional baseada em layouts XML legados e Fragments apresentava desvantagens para a manutenibilidade, reatividade e escalabilidade do projeto sob a Arquitetura Hexagonal:
1. Alto acoplamento e verbosidade na manipulação de estados de UI (`LiveData` ou callbacks manuais).
2. Dificuldade em garantir fluidez e reatividade em tempo real para buscas com *debounce* e filtragem dinâmica de listas (`Flow` / `StateFlow`).
3. Necessidade de modernizar o padrão visual utilizando Material Design 3 e um sistema de design declarativo nativo do Android.

---

## Decisões Tomadas

1. **Migração Permanente para Jetpack Compose:**
   - Remoção total dos arquivos de layout XML legados (como `activity_main.xml`).
   - Adoção de 100% de componentes declarativos em **Jetpack Compose** para toda a camada de apresentação (`adapter/input/ui`).

2. **Gerenciamento de Estado Reativo com `StateFlow` e Coroutines:**
   - Utilização de `StateFlow` nas ViewModels (`HistoryViewModel`, `BlockedContactsViewModel`, `SettingsViewModel`) para expor estados imutáveis e reativos para a UI.
   - Aplicação de operadores reativos como `flatMapLatest` e `debounce(300)` na busca de contatos bloqueados, garantindo performance e experiência de usuário fluida.

3. **Navegação com Navigation Compose e Scaffold:**
   - Implementação do `MainScreen` estruturado com um `Scaffold`, barra de navegação inferior (`NavigationBar`) e `NavHost` para gerenciar a troca entre as abas (*Settings*, *History*, *Blocked*).

4. **Metodologia TDD na Camada de Apresentação:**
   - Escrita prévia de testes unitários em JVM utilizando `runTest`, `StandardTestDispatcher` e regras dedicadas (`MainDispatcherRule`) para validar ViewModels e fluxos assíncronos.

---

## Consequências

### Positivas
* **Código Declarativo e Limpo:** Redução drástica de código boilerplate na camada de UI, eliminando a necessidade de `findViewById` ou adaptadores customizados para listas (`LazyColumn`).
* **Testabilidade Avançada:** Controle preciso do tempo virtual em testes de ViewModels e corrotinas, assegurando que fluxos com *debounce* e atualizações reativas funcionem sem falhas.
* **Alinhamento com o Ecossistema Moderno:** Preparação sólida para a evolução do aplicativo para o MVP comercial, integrando componentes visuais consistentes (Material Design 3).

### Negativas / Limitações
* Curva de aprendizado inicial para o ecossistema Compose e gerenciamento de estado reativo via `collectAsState()`.
* Necessidade de configuração rigorosa de despachantes de teste (`StandardTestDispatcher` e `runCurrent`) para evitar falhas de concorrência em testes unitários assíncronos.