# ADR 002: Migração de Persistência para Room Database e Adoção de Injeção de Dependências com Hilt

* **Status:** Aceito
* **Data:** 2026-11-02
* **Contexto:** Épico 1 - Fundação Técnica e Migração de Dados (Projeto Call Filter)

---

## Contexto e Problema

Com a evolução da POC para o MVP do aplicativo **Call Filter**, o uso do `SharedPreferences` para armazenar a lista de números excluídos/bloqueados apresentou limitações funcionais e arquiteturais:
1. Impossibilidade de realizar consultas complexas, paginação ou ordenações dinâmicas para o histórico de auditoria de chamadas (`CallLog`).
2. Necessidade de uma estrutura relacional segura e reativa (`Flow`) para suportar as futuras interfaces de UI (listas de bloqueados e histórico).
3. Forte acoplamento e instanciação manual de componentes (serviços e activities criando repositórios diretamente no código), dificultando a testabilidade unitária e violando a modularidade da Arquitetura Hexagonal.

---

## Decisões Tomadas

1. **Adoção do Room Database (com KSP):**
   - Substituição do `SharedPreferences` como fonte primária da lista de contatos bloqueados pela biblioteca oficial **Room Database** (`androidx.room`).
   - Uso do **KSP (Kotlin Symbol Processing)** em substituição ao `kapt` para maior velocidade na compilação dos DAOs e entidades.
   - Criação das entidades `BlockedContactEntity` e `CallLogEntity` e seus respectivos DAOs (`BlockedContactDao` e `CallLogDao`).

2. **Estratégia Transparente de Migração (SharedPreferences $\rightarrow$ Room):**
   - Implementação do componente `PreferencesToRoomMigrator` para extrair registros antigos salvos sob a chave `excluded_contacts` no `SharedPreferences` e migrá-los para a tabela `blocked_contacts` do Room.
   - A chave antiga do `SharedPreferences` é limpa após o sucesso do processo, garantindo que execuções subsequentes ou novas instalações (MVP) ocorram sem sobrecarga de CPU ou impactos operacionais.

3. **Injeção de Dependências com Hilt (Dagger):**
   - Configuração do **Hilt** `@HiltAndroidApp` na classe de aplicação `CallFilterApplication`.
   - Criação dos módulos `@Module` `DatabaseModule` (provendo `AppDatabase`, DAOs e `SharedPreferences`) e `RepositoryModule` (provendo as abstrações das portas de saída).
   - Refatoração dos adaptadores de entrada (`CallScreeningServiceImpl` e `MainActivity`) com a anotação `@AndroidEntryPoint` para injeção limpa de dependências via `@Inject`.

4. **Metodologia de Desenvolvimento:**
   - Todo o Épico 1 foi conduzido estritamente através do fluxo de **TDD (Test-Driven Development)**, criando os testes unitários do banco em memória e do migrador em JVM (com **Robolectric**) antes de implementar os componentes finais.

---

## Consequências

### Positivas
* **Alta Testabilidade:** DAOs e regras de migração totalmente cobertos por testes unitários locais que executam direto na JVM em poucos segundos.
* **Desacoplamento e Escalabilidade:** A camada de UI e o serviço de chamadas deixaram de instanciar dependências manualmente, dependendo apenas das abstrações injetadas pelo Hilt.
* **Preparação para o MVP:** O Room Database viabiliza a construção das próximas views do backlog (View de Bloqueados e Histórico de Chamadas com filtros e busca).

### Negativas / Limitações
* Aumento no tamanho do APK e no tempo inicial de build devido às dependências do Room, Hilt e KSP.
* Exige que novos componentes do Android (Activities, Services, Fragments) sejam devidamente anotados com `@AndroidEntryPoint` para interagir com a árvore de DI do Hilt.