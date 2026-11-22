# ADR 005: Adequação aos Requisitos de Publicação da Play Store

* **Status:** Aceito
* **Data:** 2026-11-21
* **Contexto:** Publicação inicial do Call Filter na Google Play

---

## Contexto e Problema

O aplicativo ainda usava `com.example.callfilter` como `applicationId`, target API 34 e Play Billing Library 6.2.0. O identificador `com.example` não pode ser usado para publicação, o target API 36 é exigido para novos apps e atualizações a partir de 31 de agosto de 2026, e a versão 6 da Billing Library ultrapassou seu prazo de atualização.

Era necessário adequar a identidade do app, a toolchain Android e a biblioteca de faturamento sem refatorar os pacotes Kotlin nem implementar o fluxo de compra.

## Decisões Tomadas

1. **Identidade de publicação e Firebase:**
   - Definir `applicationId = "com.ramon.callfilter"` e substituir `app/google-services.json` pelo arquivo fornecido para esse pacote.
   - Manter `namespace = "com.example.callfilter"` e os diretórios/pacotes Kotlin e de teste como estão. `applicationId` identifica o app instalado/publicado; `namespace` define os pacotes do código e das classes geradas. Não foi criado symlink nem feita refatoração de pacotes.

2. **Toolchain para API 36:**

   | Componente | Antes | Selecionado | Motivo / referência |
   |---|---|---|---|
   | AGP | 8.2.2 | 8.10.1 | Primeira linha estável com suporte a API 36. AGP 8.10 requer Gradle 8.11.1 e JDK 17. |
   | Gradle | 8.5 | 8.11.1 | Versão mínima requerida pelo AGP 8.10; wrapper configurado com checksum oficial. |
   | JDK | 17 | 21 | AGP aceita JDK 17+, mas Robolectric 4.16 requer JDK 21 para testes com SDK 36; CI atualizado para 21. |
   | Kotlin | 1.9.22 | 2.2.20 | Compatível oficialmente com AGP até 8.11.1 e Gradle até 8.14. |
   | KSP | 1.9.22-1.0.17 | 2.2.20-2.0.4 | Pareado com Kotlin 2.2.20; patch selecionado inclui correções para KSP 2.2.20. |
   | Compose Compiler | 1.5.9 via `composeOptions` | Plugin `org.jetbrains.kotlin.plugin.compose` 2.2.20 | Kotlin 2.0+ usa o plugin do compilador Compose na mesma versão do Kotlin; a referência antiga foi removida. |
   | Hilt | 2.50 | 2.56.2 | A versão anterior falhou no processamento KSP/Kotlin 2.2 (`NoClassDefFoundError` de `kotlin.reflect.full.KClasses`). |
   | Room | 2.6.1 | 2.7.1 | Room 2.6.1 falhou com KSP/Kotlin 2.2 (`unexpected jvm signature V`); 2.7.1 passou a geração de código e os testes. |
   | Robolectric | 4.11.1 | 4.16.1 | Suporta SDK 36; exige JDK 21 quando os testes usam esse SDK. |
   | `compileSdk` / `targetSdk` | 34 / 34 | 36 / 36 | Requisito de publicação da Play; `minSdk = 29` permanece. |

   Não foi necessário atualizar o Compose BOM nem outras dependências AndroidX.

3. **Play Billing Library:**
   - Usar a versão estável mais recente indicada pelo Google, `9.1.0`, na coordenada Kotlin `com.android.billingclient:billing-ktx`.
   - Manter uma única versão e um único alias no catálogo.
   - Migrar `enablePendingPurchases()` para `PendingPurchasesParams` com `enableOneTimeProducts()`, conforme indicado para produtos de compra única.
   - Preservar as assinaturas públicas e o comportamento atual de `PlayBillingAdapter`; o fluxo de compra não faz parte desta decisão.

4. **CI e validações de plataforma:**
   - Usar JDK 21 no job `test-and-build`.
   - Disparar o mesmo job nos alvos de PR empilhados A, B e C, garantindo os gates na ordem de dependência.
   - `MainScreen` mantém o padding fornecido pelo `Scaffold`; `NavHost` permanece responsável pela navegação e pelo back. A revisão do `CallScreeningService` e da solicitação de `ROLE_CALL_SCREENING` não indicou mudanças de API necessárias.
   - O APK validado não contém bibliotecas `.so`; `zipalign -c -P 16 -v 4` confirmou o alinhamento de 16 KB. O smoke test em emulador/API 36 ainda depende de validação manual.

## Consequências

### Positivas
* O APK usa `com.ramon.callfilter`, `targetSdk 36`, `minSdk 29` e Billing 9.1.0 com o metadado de versão esperado pela Play.
* A arquitetura e os pacotes Kotlin existentes são preservados.
* Testes unitários e build release passam com a toolchain atualizada.

### Negativas / Limitações
* Os testes e o CI passam a exigir JDK 21.
* Não há emulador API 36 neste ambiente; edge-to-edge e o fluxo de call screening precisam de smoke test manual.
* A compra Premium continua indisponível: `launchBillingFlow` é stub, `startConnection` não é chamado pela aplicação, não há `acknowledgePurchase` nem reconexão, e o status Premium é guardado localmente sem validação adicional. A implementação funcional deve ser tratada como épico separado.

## Fontes

* [Requisitos de target API da Play](https://developer.android.com/google/play/requirements/target-sdk)
* [Notas do AGP 8.10 e compatibilidade](https://developer.android.com/build/releases/agp-8-10-0-release-notes)
* [Compatibilidade do Kotlin Gradle Plugin com Gradle e AGP](https://kotlinlang.org/docs/gradle-configure-project.html#apply-the-plugin)
* [Compose Compiler Gradle Plugin](https://developer.android.com/develop/ui/compose/compiler)
* [KSP 2.2.20-2.0.4](https://github.com/google/ksp/releases/tag/2.2.20-2.0.4)
* [Room release notes](https://developer.android.com/jetpack/androidx/releases/room)
* [Dagger/Hilt 2.56.2](https://github.com/google/dagger/releases/tag/dagger-2.56.2)
* [Robolectric 4.16](https://github.com/robolectric/robolectric/releases/tag/robolectric-4.16)
* [Play Billing release notes](https://developer.android.com/google/play/billing/release-notes)
* [Migração Play Billing 9](https://developer.android.com/google/play/billing/migrate-gpblv9)
* [Prazo de descontinuação da Play Billing Library](https://developer.android.com/google/play/billing/deprecation-faq)
