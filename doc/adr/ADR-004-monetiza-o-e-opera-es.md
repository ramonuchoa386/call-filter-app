# ADR 004: Estratégia de Monetização, AdMob Wrapper, Google Play Billing e Firebase Remote Config

* **Status:** Aceito
* **Data:** 2026-11-15
* **Contexto:** Épico 3 - Estratégia de Monetização, Wrapper Global de Anúncios e Operações Dinâmicas (Projeto Call Filter)

---

## Contexto e Problema

Com a estabilização da fundação técnica (Room, Hilt) e a modernização da interface em Jetpack Compose (Épicos 1 e 2), o aplicativo **Call Filter** atingiu maturidade para evoluir para um modelo de produto comercializável. Os principais desafios a serem resolvidos eram:
1. A necessidade de rentabilizar a versão gratuita do aplicativo através de anúncios não obstrutivos.
2. Oferecer aos usuários uma alternativa de monetização via compra dentro do aplicativo (*In-App Purchase*) para a remoção definitiva de anúncios (versão Premium).
3. Garantir flexibilidade operacional para controlar parâmetros e ativar/desativar recursos (*Feature Flags*) remotamente sem a obrigatoriedade de publicar novas versões do APK nas lojas.

---

## Decisões Tomadas

1. **Wrapper Global de Anúncios com AdMob e Jetpack Compose:**
   - Criação do componente `AdBannerView` encapsulando o `AdView` da SDK oficial da Google AdMob (`play-services-ads`) por meio de um `AndroidView` compatível com Compose.
   - Integração estrutural no `topBar` do `Scaffold` principal (`MainScreen`), permitindo a exibição dinâmica e reativa de um banner fixo no topo.

2. **Gerenciamento do Status Premium (Google Play Billing & SharedPreferences Adapter):**
   - Adoção da biblioteca `billing-ktx` para gerenciar o fluxo de compra de produtos (*In-App*) de remoção de anúncios (`remove_ads_product`).
   - Implementação do `SharedPreferencesPremiumRepositoryImpl` e da porta de saída `PremiumRepository` para persistência local e checagem rápida do status de assinatura/compra.

3. **Operações Dinâmicas com Firebase Remote Config:**
   - Integração do Firebase BoM e Remote Config (`firebase-config-ktx`) para prover a *Feature Flag* `admob_enabled`, permitindo governança remota sobre a exibição de anúncios.
   - Atualização reativa da `MonetizationViewModel` utilizando `StateFlow` para coordenar o estado Premium e o Remote Config, expandindo automaticamente o conteúdo das abas ao topo quando os anúncios estão inativos ou o usuário é Premium.

4. **Condução por TDD e Arquitetura Hexagonal:**
   - Isolamento das regras de negócio de monetização no pacote `domain/` através de portas de entrada (`CheckPremiumStatusUseCase`, `SetPremiumStatusUseCase`) testadas unitariamente com fakes na JVM.

---

## Consequências

### Positivas
* **Flexibilidade Comercial:** Permite monetizar tráfego gratuito e oferece uma excelente experiência ad-free aos compradores da versão Premium.
* **Controle Operacional Avançado:** O Firebase Remote Config possibilita ligar ou desligar campanhas ou exibições de anúncios em tempo real.
* **Arquitetura Preservada:** Os SDKs externos do AdMob, Billing e Firebase foram confinados estritamente aos adaptadores de infraestrutura (`adapter/output`), mantendo o domínio de negócio limpo e 100% testável.

### Negativas / Limitações
* Aumento no escopo de dependências do projeto e necessidade de configuração correta do arquivo `google-services.json` em ambientes de build oficiais.
* Complexidade adicional no gerenciamento de estados reativos na UI para transições dinâmicas do banner.