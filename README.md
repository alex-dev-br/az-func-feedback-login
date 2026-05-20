# 🔑 Azure Function - Feedback Platform Login

[![Build Status](https://github.com/KervinCandido/az-func-feedback-login/actions/workflows/main_func-feedback-login.yml/badge.svg)](https://github.com/KervinCandido/az-func-feedback-login/actions)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.35.3-blueviola.svg?style=flat&logo=quarkus)](https://quarkus.io/)
[![Java](https://img.shields.io/badge/Java-25-orange.svg?style=flat&logo=openjdk)](https://openjdk.org/)
[![H2 Database](https://img.shields.io/badge/H2-In--Memory-blue.svg?style=flat)](https://www.h2database.com/)
[![OpenTelemetry](https://img.shields.io/badge/OpenTelemetry-Enabled-green.svg?style=flat&logo=opentelemetry)](https://opentelemetry.io/)
[![Jacoco Coverage](https://img.shields.io/badge/cobertura-%E2%89%A580%25-brightgreen.svg?style=flat)](https://www.eclemma.org/jacoco/)

Esta é uma **Azure Function** desenvolvida com o framework **Quarkus** dedicada à autenticação de usuários e emissão de tokens JWT para o ecossistema da Plataforma de Feedback.

> [!NOTE]
> **Objetivo do Projeto:** Este projeto possui caráter puramente demonstrativo e didático. Seu objetivo é ilustrar como funcionaria o fluxo de autenticação e geração de tokens em um cenário corporativo real utilizando arquitetura serverless (Azure Functions), integração com banco de dados em memória e observabilidade completa via OpenTelemetry.

---

## 🏗️ Arquitetura e Tecnologias

A aplicação foi desenhada seguindo as boas práticas de **Clean Architecture** e princípios de **Domain-Driven Design (DDD)** para manter as regras de negócio isoladas de detalhes de infraestrutura.

- **Quarkus 3.35.3** & **Java 25**: Stack moderna otimizada para alto desempenho, baixo tempo de inicialização (*cold start*) e baixo consumo de memória, características cruciais para arquitetura serverless.
- **Azure Functions (HTTP Binding)**: Implementado através da biblioteca `quarkus-azure-functions-http`, que permite expor recursos padrão do JAX-RS (`quarkus-rest`) como gatilhos HTTP da Azure Function.
- **MicroProfile JWT Build & Sign**: Geração robusta e assinatura de tokens JWT usando criptografia assimétrica RSA com algoritmos `RSA-OAEP-256` e `A256CBC-HS512`.
- **Flyway**: Gerenciador de migração de banco de dados, encarregado de criar a estrutura de tabelas e carregar a carga de dados inicial automaticamente na inicialização da aplicação.
- **H2 Database (In-Memory)**: Banco de dados relacional ágil que roda inteiramente em memória, ideal para ambientes de demonstração, desenvolvimento rápido e testes de integração rápidos.
- **OpenTelemetry & Micrometer**: Observabilidade nativa integrada para geração e exportação de traços (*traces*), métricas e logs.

---

## 🗄️ Banco de Dados & Carga de Dados Inicial

O projeto utiliza um banco de dados em memória **H2** (`jdbc:h2:mem:feedbackdb`) configurado para rodar o **Flyway** automaticamente no início da aplicação (`quarkus.flyway.migrate-at-start=true`).

A estrutura do banco conta com a tabela `tb_user` (`src/main/resources/db/migration/V1__create_user_table.sql`):

```sql
CREATE TABLE tb_user (
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL
);
```

### 👤 Usuários Pré-definidos
Para fins de demonstração e testes imediatos, o banco é populado automaticamente com os seguintes usuários:

| Usuário | Senha (Plain Text) | Perfil (User Type / Role) |
| :--- | :--- | :--- |
| `aluno` | `senha123` | `ALUNO` |
| `professor` | `senha123` | `PROFESSOR` |
| `admin` | `senha123` | `ADMIN` |

> [!WARNING]
> **Segurança de Demonstração:** Por motivos de simplificação didática, as senhas no banco estão gravadas em texto puro (plain text) e a validação é feita via comparação simples de strings (`String::equals`). Em uma implementação de produção real, as senhas seriam obrigatoriamente submetidas a algoritmos seguros de hashing com sal (ex: *BCrypt* ou *PBKDF2*).

---

## 🚀 Endpoint da Azure Function (Autenticação)

A função expõe um único endpoint HTTP do tipo **POST** para realizar a autenticação.

### 🌐 Rota
`POST /api/sign-in`

- **Content-Type:** `application/json`
- **Accept:** `application/json`

---

### 📥 Exemplo de Requisição (Request Payload)

```json
{
  "username": "aluno",
  "password": "senha123"
}
```

#### Regras de Validação de Payload:
- `username`: Campo obrigatório. Tamanho de 3 a 20 caracteres.
- `password`: Campo obrigatório. Tamanho de 6 a 30 caracteres.

---

### 📤 Exemplo de Resposta - Sucesso (200 OK)

Quando as credenciais fornecidas são válidas, a aplicação retorna o token JWT assinado e seus metadados:

```json
{
  "type": "Bearer",
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhbHVubyIsImdyb3VwcyI6WyJBTFVOTyJdLCJpc3MiOiJodHRwczovL2ZlZWRiYWNrLWxvZ2luLmNvbS5ici9pc3N1ZXIiLCJpYXQiOjE3MTYxNzQ0MDAsImV4cCI6MTcxNjIxNzYwMH0.signature...",
  "issuedAt": 1716174400,
  "expiresAt": 1716217600
}
```

> [!TIP]
> **Conteúdo do Token (Claims):**
> O token JWT gerado possui o algoritmo de criptografia configurado e carrega as seguintes informações de identificação:
> - `sub`: Nome do usuário autenticado (ex: `aluno`).
> - `groups`: Perfil do usuário mapeado no sistema (ex: `ALUNO`), permitindo controle de acesso baseado em papéis (RBAC - *Role-Based Access Control*) em outros microsserviços.
> - `iss` (*Issuer*): Definido como `https://feedback-login.com.br/issuer`.
> - `exp` (*Expiration*): O tempo de expiração padrão do token é de **12 horas** (`43200` segundos).

---

### ❌ Exemplos de Resposta - Erro

#### 1. Usuário Não Encontrado ou Senha Incorreta (`401 Unauthorized`)
Retorna resposta vazia com código HTTP `401`.

#### 2. Falha de Validação dos Campos (`400 Bad Request`)
Quando os dados não atendem os critérios mínimos de tamanho ou formatação:

```json
{
  "title": "Erro na validação dos campos",
  "details": [
    "username: deve corresponder a entre 3 e 20 caracteres",
    "password: não deve ser nulo"
  ]
}
```

---

### 💻 Como Testar via Terminal (cURL)

```bash
curl -X POST http://localhost:8080/api/sign-in \
  -H "Content-Type: application/json" \
  -d '{"username": "aluno", "password": "senha123"}'
```

---

## 📊 Métricas e OpenTelemetry

O projeto implementa observabilidade de nível corporativo usando **OpenTelemetry** e **Micrometer**, exportando dados para o **Azure Application Insights** nativamente por meio do `quarkus-opentelemetry-exporter-azure`.

### 🛡️ Prefixo das Métricas
Para evitar poluição no monitoramento e facilitar a criação de painéis (*dashboards*), todas as métricas da aplicação são interceptadas e prefixadas dinamicamente com **`feedback.login.`** graças à classe `MetricPrefixConfig`.

### 📈 Principais Métricas Exportadas

A tabela a seguir descreve as principais métricas customizadas coletadas pela aplicação durante sua operação:

| Métrica Completa | Tipo | Descrição |
| :--- | :--- | :--- |
| `feedback.login.logins.attempted` | `Counter` | Quantidade acumulada de tentativas de login, independentemente se foram bem-sucedidas ou falhas. |
| `feedback.login.logins.duration` | `Timer` / `Histogram` | Medição e distribuição do tempo de processamento completo da requisição de login. |
| `feedback.login.jwt.generate` | `Counter` | Quantidade acumulada de tokens JWT que foram gerados e assinados com sucesso. |
| `feedback.login.jwt.generate.duration` | `Timer` / `Histogram` | Tempo necessário para assinar digitalmente e construir a estrutura do token JWT. |
| `feedback.login.http.server.requests` | `Timer` / `Histogram` | Estatísticas globais de requisições recebidas pelo servidor HTTP do Quarkus (tempo de resposta, status HTTP, etc.). |

> [!NOTE]
> Adicionalmente, métricas padrão do ecossistema JVM e do sistema operacional (como telemetria de Garbage Collection, uso de threads, memória heap utilizada e uso de CPU) também são exportadas automaticamente com o prefixo corporativo.

---

## 🧪 Cobertura de Testes

Este projeto possui uma robusta suite de testes automatizados composta por testes unitários e de integração, garantindo que as regras de negócio e integrações de infraestrutura funcionem conforme o planejado.

- **Frameworks:** JUnit 5, RestAssured (para testes HTTP de integração), Mockito (para isolamento e dublês de testes) e AssertJ.
- **Garantia de Qualidade (JaCoCo):** O plugin oficial do JaCoCo está incorporado ao ciclo de construção do Maven. Foi configurada uma barreira severa na compilação do projeto exigindo **no mínimo 80% de cobertura** para as seguintes métricas:
  - **Linhas de Código** (Line Coverage)
  - **Desvios de Código** (Branch/Decision Coverage)
  - **Métodos** (Method Coverage)

Se o código sofrer alterações que derrubem a cobertura para menos de 80%, a compilação do Maven falhará no estágio de `package`.

### 🛠️ Comandos Úteis

#### Rodar todos os testes com relatório do JaCoCo:
```bash
mvn clean test
```

#### Executar em Modo de Desenvolvimento Local (Quarkus Dev Mode):
Este modo inicia a Azure Function localmente na porta `8080` com recursos adicionais do Quarkus, como *live reload* automático de código e interface gráfica de desenvolvimento (Dev UI).
```bash
mvn quarkus:dev
```

#### Gerar Pacote Final para Deploy (Package):
Compila a aplicação, executa as validações do JaCoCo e empacota a Azure Function pronta para ser publicada na nuvem Azure.
```bash
mvn clean package
```
O diretório do artefato empacotado e preparado para deploy na Azure será gerado em: `target/azure-functions/func-feedback-platform-login`.

---

## 🔑 Segurança e Criptografia (Key Vault)

No ambiente local ou de testes, o Quarkus utiliza arquivos locais contendo chaves públicas/privadas RSA para a assinatura dos tokens JWT.

Para o ambiente de nuvem Azure, a segurança de chaves é tratada de forma nativa e profissional:
- Os segredos e chaves nunca ficam expostos no repositório.
- A aplicação utiliza o `quarkus-azure-keyvault` para ler dinamicamente a chave pública e a chave privada do **Azure Key Vault**:
  - `mp.jwt.verify.publickey=${kv//jwt-public-key:}`
  - `smallrye.jwt.sign.key=${kv//jwt-private-key:}`
- A autenticação entre a Azure Function e o Azure Key Vault é realizada de forma integrada e segura por meio de **Managed Identity** gerenciada pela própria Azure.
