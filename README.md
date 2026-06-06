# CRM Assistente PoC

PoC academica para demonstrar o fluxo principal de um assistente inteligente integrado a um CRM juridico ficticio.

Fluxo validado:

```text
Entrada do usuario
-> interpretacao de intencao
-> extracao de entidades
-> query segura parametrizada
-> verificacao simples de permissao
-> consulta ao banco
-> formatacao da resposta
-> log/auditoria
```

## Decisoes tecnicas

- Java 17 com Spring Boot.
- H2 em memoria para simplificar a demonstracao.
- Spring Data JPA com queries parametrizadas por intencao.
- Interpretador por regras e expressoes regulares.
- Sem LLM gerando SQL livremente.
- Carga inicial de dados ficticios via `CommandLineRunner`.
- O perfil informado na requisicao e aceito para a demo, mas o perfil cadastrado no banco tem precedencia na permissao.

## Como rodar

```bash
mvn spring-boot:run
```

A API fica disponivel em:

```text
http://localhost:8080
```

## Endpoints

### Consulta por texto

`POST /api/consulta`

```bash
curl -X POST http://localhost:8080/api/consulta \
  -H "Content-Type: application/json" \
  -d "{\"usuario\":\"advogado_1\",\"perfil\":\"ADVOGADO\",\"mensagem\":\"Qual o status do processo 12345?\"}"
```

### Consulta por audio simulado

`POST /api/consulta-audio`

```bash
curl -X POST http://localhost:8080/api/consulta-audio \
  -H "Content-Type: application/json" \
  -d "{\"usuario\":\"advogado_1\",\"perfil\":\"ADVOGADO\",\"transcricaoSimulada\":\"Quem e o responsavel pelo processo 12345?\"}"
```

### Webhook WhatsApp simulado

#### Verificacao do webhook pela Meta

`GET /api/webhook/whatsapp`

Token de verificacao configurado na PoC:

```text
tcc-poc-whatsapp-verify-token
```

Na tela da Meta, use:

```text
Callback URL: https://SUA-URL-PUBLICA/api/webhook/whatsapp
Verify token: tcc-poc-whatsapp-verify-token
```

Teste local da verificacao:

```bash
curl "http://localhost:8080/api/webhook/whatsapp?hub.mode=subscribe&hub.verify_token=tcc-poc-whatsapp-verify-token&hub.challenge=123456"
```

O retorno esperado e:

```text
123456
```

#### Recebimento simulado de mensagem

`POST /api/webhook/whatsapp`

```bash
curl -X POST http://localhost:8080/api/webhook/whatsapp \
  -H "Content-Type: application/json" \
  -d "{\"from\":\"whatsapp:+5533999999999\",\"body\":\"Qual o status do processo 12345?\",\"usuario\":\"advogado_1\"}"
```

## Cenarios de teste
### Testes feitos utilizando [Bruno API](https://www.usebruno.com/)

### 1. Status do processo permitido

```bash
POST http://localhost:8080/api/consulta
"Content-Type: application/json"
json body{
{
  "usuario": "advogado_1",
  "perfil": "ADVOGADO",
  "mensagem": "Qual o status do processo 12345?"
}
```
![Processo permitido - Bruno API](imgs\proc_perm.PNG)

### 2. Responsável pelo processo

```bash
POST http://localhost:8080/api/consulta
"Content-Type: application/json"
json body{
{
  "usuario": "advogado_1",
  "perfil": "ADVOGADO",
  "mensagem": "Quem e o responsavel pelo processo 12345?"
}
```
![Responsavel Processo - Bruno API](imgs\resp_proc.PNG)

### 3. Dados do cliente

```bash
POST http://localhost:8080/api/consulta
"Content-Type: application/json"
json body{
{
  "usuario": "advogado_1",
  "perfil": "ADVOGADO",
  "mensagem": "Qual o telefone do cliente Joao Silva?"
}
```
![Dados cliente - Bruno API](imgs\tele_clien.PNG)

### 4. Processos de uma cliente

```bash
POST http://localhost:8080/api/consulta
"Content-Type: application/json"
json body{
{
  "usuario": "advogado_1",
  "perfil": "ADVOGADO",
  "mensagem": "Quais processos sao do cliente Maria Souza?"
}
```
![Processos cliente - Bruno API](imgs\proc_clien.PNG)

### 5. Bloqueio por permissão

```bash
POST http://localhost:8080/api/consulta
"Content-Type: application/json"
json body{
{
  "usuario": "assistente_1",
  "perfil": "ASSISTENTE",
  "mensagem": "Qual o status do processo 54321?"
}
```
![Bloqueio permissão - Bruno API](imgs\proc_nega.PNG)

### 6. Intenção não reconhecida

```bash
POST http://localhost:8080/api/consulta
"Content-Type: application/json"
json body{
{
  "usuario": "assistente_1",
  "perfil": "ASSISTENTE",
  "mensagem": "Bom dia?"
}
```
![Intenção não reconhecida - Bruno API](imgs\inten_nao_recon.PNG)

## Estrutura

```text
src/main/java/br/edu/tcc/crmassistente
  config
  controller
  dto
  model
  repository
  service
```

## Observacao sobre SQL seguro

As intencoes reconhecidas nao viram SQL livre. Cada intencao chama um metodo especifico de repositorio, como:

- `ProcessoRepository.findByNumero(numero)`
- `ClienteRepository.buscarPorNomeNormalizado(nomeNormalizado)`
- `ProcessoRepository.listarPorNomeClienteNormalizado(nomeNormalizado)`
- `PermissaoProcessoRepository.usuarioTemPermissaoNoProcesso(username, numeroProcesso)`

Essas consultas usam parametros nomeados ou metodos do Spring Data, evitando concatenacao de texto da pergunta na query.
