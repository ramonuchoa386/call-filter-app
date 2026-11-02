# ADR 001: Adoção de Arquitetura Hexagonal e Android CallScreeningService API

* **Status:** Aceito
* **Data:** 2026-03-30
* **Contexto:** Projeto Call Filter (Android)

---

## Contexto e Problema

O aplicativo necessita interceptar chamadas telefônicas recebidas em tempo real e decidir pelo bloqueio ou liberação do toque com base nas configurações do usuário (se o número é um contato salvo ou se está em uma lista de exceção). 

Para garantir a sustentabilidade do código, facilidade de manutenção e altíssima velocidade na execução dos testes unitários, era necessário separar a regra de decisão das APIs proprietárias do sistema operacional Android. Além disso, as versões modernas do Android (API 29+) impõem restrições rigorosas sobre como chamadas podem ser interceptadas.

---

## Decisões Consideradas

1. **Estrutura Arquitetural:**
   - *Opção A:* Arquitetura tradicional MVVM com acoplamento direto das APIs Android nas ViewModels/Repositories.
   - *Opção B (Escolhida):* **Arquitetura Hexagonal (Ports & Adapters)**, mantendo a regra de decisão em um Core puro em Kotlin e isolando o Android nos Adapters.

2. **API de Interceptação de Chamadas Android:**
   - *Opção A:* Uso legacy de `BroadcastReceiver` ouvindo `ACTION_PHONE_STATE_CHANGED` combinado com reflexão (incompatível/descontinuado a partir do Android 10).
   - *Opção B (Escolhida):* Implementação da API oficial **`CallScreeningService`** (`RoleManager.ROLE_CALL_SCREENING`) introduzida no Android 10 (API 29+).

---

## Consequências

### Positivas
* **Testabilidade Imparável:** Toda a lógica de bloqueio de chamadas pode ser testada em milissegundos via testes JVM puros sem subir emuladores.
* **Conformidade com a Google Play:** O uso do `CallScreeningService` é o padrão recomendado pela Google para apps de filtragem de spams e bloqueio, facilitando a aprovação da aplicação.
* **Desacoplamento:** Mudar o meio de persistência (de `SharedPreferences` para `Room`) exige alteração apenas na camada de `adapter/output`, sem qualquer impacto nas regras de negócio.

### Negativas / Limitações
* Exige `minSdk = 29` (Android 10 ou superior), limitando o suporte a dispositivos com versões legadas do sistema operacional.
* Necessita solicitar explicitamente ao usuário a concessão da *Role* de aplicativo de filtragem padrão de chamadas durante o primeiro acesso.