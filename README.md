# Call Filter - Rejeitador de Chamadas (Android)

O **Call Filter** é um aplicativo Android nativo desenvolvido em **Kotlin**, utilizando **Arquitetura Hexagonal (Ports & Adapters)** e **TDD (Test-Driven Development)**. O objetivo principal do aplicativo é interceptar chamadas recebidas antes de o aparelho tocar e rejeitar automaticamente números desconhecidos (que não constam na agenda de contatos) ou números específicos presentes na lista de bloqueio/exceção.

---

## 🚀 Funcionalidades Atuais

- **Rejeição Automática de Não-Contatos:** Alternador para ativar/desativar o bloqueio global de qualquer chamada recebida cujo número não esteja salvo na agenda nativa do dispositivo.
- **Bloqueio/Desbloqueio de Contato Específico:** Permite adicionar ou remover um número de telefone específico a uma lista de exceção mantida no aplicativo.
- **Integração com Android CallScreeningService (API 29+ / Android 10+):** Utiliza as APIs modernas do Android para avaliação nativa e imediata de chamadas de entrada.
- **Solicitação Dinâmica de Permissões e Roles:** Interface integrada para concessão de permissão de leitura de contatos (`READ_CONTACTS`) e solicitação da atribuição da *Role* do sistema `ROLE_CALL_SCREENING`.

---

## 🏗️ Arquitetura do Projeto (Hexagonal)

A estrutura separa estritamente as regras de negócio puras (Core) das APIs de infraestrutura do sistema operacional Android:

```text
com.example.callfilter/
├── domain/                      # CORE / DOMÍNIO (Kotlin Puro)
│   ├── model/                  # Entidades (CallSettings)
│   └── port/
│       ├── input/              # Casos de Uso (EvaluateCallUseCase, etc.)
│       └── output/             # Interfaces (CallSettingsRepository, ContactLookupPort)
│
└── adapter/                     # ADAPTADORES (Infraestrutura Android)
    ├── input/                  # Framework UI (MainActivity) e Serviço (CallScreeningServiceImpl)
    └── output/                 # Implementações (SharedPreferences, ContentResolver)

```

---

## 🛠️ Pré-requisitos de Ambiente (Linux Ubuntu)

Certifique-se de que o seu ambiente possui os binários e variáveis configurados:

* **JDK 17** (`java -version`)
* **Android SDK** com `platform-tools` (`adb --version`) e `cmdline-tools`
* **Gradle Wrapper** (`./gradlew`)

---

## 🧪 1. Como Executar os Testes Unitários (TDD)

Os testes de domínio não possuem dependência de emuladores ou dispositivos físicos e rodam diretamente na JVM em segundos.

No terminal, na raiz do projeto:

```bash
# Executar todos os testes unitários da camada de domínio
./gradlew test

```

Os relatórios detalhados dos testes em HTML serão gerados em:
`app/build/reports/tests/testDebugUnitTest/index.html`

---

## 📦 2. Como Gerar o APK da Aplicação

Para compilar o projeto e gerar o executável APK de depuração:

```bash
# Compilar e gerar o APK de Debug
./gradlew assembleDebug

```

O arquivo `.apk` compilado estará disponível no caminho:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 📱 3. Como Rodar no Android Físico via USB

1. **Ativar o Modo Desenvolvedor no Celular:**
* Vá em **Configurações > Sobre o Telefone**.
* Toque 7 vezes em **Número da Versão** até surgir a mensagem "Você agora é um desenvolvedor".
* Vá em **Configurações > Sistema > Opções do Desenvolvedor** e ative a **Depuração USB**.


2. **Conectar e Autorizar:**
* Conecte o dispositivo via cabo USB ao Ubuntu.
* No terminal Linux, verifique se o dispositivo é reconhecido:
```bash
adb devices

```


* Aceite a caixa de diálogo "Permitir depuração USB?" na tela do smartphone.


3. **Instalar o APK no Dispositivo:**
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk

```



---

## 🖥️ 4. Como Rodar em um Dispositivo Virtual (Emulador/AVD)

### Criar e Iniciar o Emulador via CLI:

1. **Baixar Imagem do Sistema e Criar AVD:**
```bash
# Baixar a imagem do sistema Android 34
sdkmanager "system-images;android-34;google_apis;x86_64"

# Criar o dispositivo virtual
avdmanager create avd -n TestDevice -k "system-images;android-34;google_apis;x86_64"

```


2. **Iniciar o Emulador:**
```bash
emulator -avd TestDevice -netdelay none -netspeed full

```


3. **Instalar o App no Emulador:**
Com o emulador rodando na tela, em outro terminal execute:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk

```



---

## 📞 5. Testando o Bloqueio de Chamadas no Emulador

Você pode simular chamadas recebidas para testar a rejeição em tempo real:

1. Abra o aplicativo instalado e **conceda a permissão de leitor de contatos** e de **App Padrão de Bloqueio de Chamadas (RoleManager)**.
2. Ative a chave **"Rejeitar chamadas de não contatos"**.
3. No terminal, envie uma chamada simulada de um número não gravado no emulador via `adb`:
```bash
adb shell gsm call +5521999999999

```


4. O app chamará o `CallScreeningServiceImpl` e a ligação será rejeitada automaticamente.

---

## 📌 Backlog Inicial do Projeto

Abaixo estão as próximas entregas e evoluções planejadas para o produto:

* [ ] **View de Lista de Contatos Bloqueados:** Interface visual dedicada para exibição dos números bloqueados, contendo filtro de ordenação e campo de busca por nome/número.
* [ ] **View de Histórico de Chamadas Interceptadas:** Tela com log de chamadas recebidas, informando data/hora e permitindo filtragem entre chamadas *Aceitas* e *Bloqueadas*.
* [ ] **Evolução de UX/UI:** Implementação de navegação por Tabs/Bottom Navigation, biblioteca de componentes visuais modernizada e refinamentos estéticos.
* [ ] **Débitos Técnicos e Infraestrutura:** Migração do armazenamento local para Room Database, implementação de testes e2e (Espresso) e expansão da cobertura unitária.
* [ ] **Central de Preferências de Notificações:** Opções para personalizar alertas/notificações quando uma chamada for bloqueada em segundo plano.
* [ ] **Espaço para Anúncios:** Integração com rede de anúncios (AdMob) para monetização da versão gratuita.
* [ ] **Integração Firebase:** Conexão com Firebase Remote Config para resgatar configurações e feature flags do aplicativo dinamicamente.

---

## 📄 Licença

Este projeto é disponibilizado sob a licença [MIT](https://www.google.com/search?q=LICENSE).