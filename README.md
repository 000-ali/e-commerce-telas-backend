#  ArteDeco — E-Commerce de Pinturas Clássicas

Projeto Integrador de Módulo | Análise e Desenvolvimento de Sistemas  
Pontifícia Universidade Católica de Goiás — 2026

## Equipe
Aline de Oliveira Tiburcio Souza -- 
Cecília Vieira Braga

## Sobre o Projeto

O *ArteDeco* é um sistema de e-commerce voltado para a venda de pinturas clássicas. O projeto valida a arquitetura do sistema através de uma estrutura em camadas, interface visual funcional e planejamento completo de qualidade.

### Funcionalidades principais:
- Catálogo de pinturas com busca por nome e filtro por artista
- Carrinho de compras persistido no banco de dados
- Checkout com Pix, Cartão de Crédito e Boleto
- Notificações assíncronas via ActiveMQ (padrão Observer)
- Autenticação com JWT e controle de acesso por perfil
- Painel administrativo para gerenciamento de produtos

## Stack Tecnológica

### Backend
- **Java 17** + **Spring Boot 3.2.5**
- **Spring Security** + **JWT (jjwt 0.12.3)**
- **Spring Data JPA** + **Hibernate**
- **ActiveMQ** — mensageria assíncrona
- **Lombok** — redução de boilerplate
- **PostgreSQL** — banco de dados

### Frontend
- **React 18** + **TypeScript**
- **Vite 7** — bundler
- **React Router 6** — roteamento
- **Axios** — requisições HTTP

### Infraestrutura
- **Docker** — container do ActiveMQ
- **Git** — versionamento com Conventional Commits

## Arquitetura

```
Cliente (Navegador :5173)
    ↓ HTTP/REST
Backend Spring Boot (:8085)
    ↓ JPA/Hibernate
PostgreSQL (:5432)
    ↓ JMS/ActiveMQ
ActiveMQ Broker (:61616)
    ↓ Consumer processa
NotificacaoService (Observer)
```

### Padrões de Projeto
| Padrão | Onde é usado |
|--------|-------------|
| **Observer** | NotificacaoService — notifica clientes sobre pedidos e promoções |
| **Strategy** | FormaPagamento — Pix, Cartão e Boleto intercambiáveis |
| **Factory Method** | Criação centralizada de objetos do sistema |
| **Producer/Consumer** | PedidoProducer e PedidoConsumer via ActiveMQ |


## Como Rodar o Projeto

### Pré-requisitos
- Java 17+
- Maven 3.x
- Node.js 18+
- PostgreSQL instalado e rodando
- Docker instalado

### 1. Subir o ActiveMQ
```bash
docker run -d --name activemq -p 61616:61616 -p 8161:8161 rmohr/activemq
```

### 2. Criar o banco de dados
```sql
CREATE DATABASE ecommerce_telas;
```

### 3. Configurar o application.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_telas
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
spring.activemq.broker-url=tcp://localhost:61616
spring.activemq.user=admin
spring.activemq.password=admin
app.queue.pedidos=fila.pedidos
jwt.secret=ecommerce-telas-chave-secreta-super-segura-2026
jwt.expiration=86400000
```

### 4. Rodar o Backend
```bash
cd telas-backend/telas-backend
mvn clean spring-boot:run
```

### 5. Rodar o Frontend
```bash
cd artedeco
npm install
npm run dev
```

### 6. Acessar o sistema
| Serviço | URL |
|---------|-----|
| Frontend | http://localhost:5173 |
| Backend API | http://localhost:8085 |
| ActiveMQ Admin | http://localhost:8161/admin |


## Credenciais Padrão

| Perfil | E-mail | Senha |
|--------|--------|-------|
| Administrador | admin@telas.com | admin123 |
| ActiveMQ | admin | admin |

> O Administrador é criado automaticamente pelo `DataInitializer` na primeira inicialização.


## Principais Endpoints da API

### Autenticação
```
POST /auth/cadastro   → cadastrar novo usuário
POST /auth/login      → autenticar e obter token JWT
```

### Produtos (público)
```
GET /produtos                    → listar disponíveis
GET /produtos/{id}               → detalhe
GET /produtos/buscar?nome=...    → pesquisa por nome
GET /produtos/artista?nome=...   → filtro por artista
```

### Produtos (admin)
```
POST   /produtos/admin           → cadastrar
PUT    /produtos/admin/{id}      → editar
DELETE /produtos/admin/{id}      → remover
```

### Carrinho
```
GET    /carrinho/{clienteId}                  → visualizar
POST   /carrinho/{clienteId}/adicionar        → adicionar item
DELETE /carrinho/{clienteId}/item/{itemId}    → remover item
POST   /carrinho/{clienteId}/finalizar        → finalizar compra
```

### Pedidos
```
GET /pedidos/{id}            → buscar pedido
GET /pedidos/cliente/{id}    → pedidos do cliente
```


## Fluxo de uma Compra

```
1. POST /auth/login              → token JWT
2. GET  /produtos                → catálogo
3. POST /carrinho/{id}/adicionar → adicionar ao carrinho
4. POST /carrinho/{id}/finalizar → finalizar
   └── Pedido salvo no banco
   └── Mensagem publicada no ActiveMQ
   └── Consumer processa e atualiza status
   └── Cliente recebe notificação
```

## Estrutura de Pastas

```
e-commerce-telas-backend/
├── telas-backend/          ← Projeto Spring Boot
│   ├── src/main/java/com/ecommerce/telas_backend/
│   │   ├── config/         ← Security, JWT, ActiveMQ, CORS
│   │   ├── controller/     ← Endpoints REST
│   │   ├── consumer/       ← Consumidor da fila ActiveMQ
│   │   ├── dto/            ← Objetos de transferência
│   │   ├── model/          ← Entidades JPA
│   │   ├── producer/       ← Produtor da fila ActiveMQ
│   │   ├── repository/     ← Interfaces JPA
│   │   └── service/        ← Lógica de negócio
│   └── pom.xml
└── artedeco/               ← Projeto React
    ├── src/
    │   ├── api.ts          ← Configuração Axios
    │   ├── contexto/       ← Estado global
    │   ├── paginas/        ← Páginas da aplicação
    │   └── componentes/    ← Componentes reutilizáveis
    └── package.json
```

## Testes

23 cenários de teste validados cobrindo os módulos de:
- Autenticação (CT-001 a CT-004)
- Catálogo (CT-005 a CT-009)
- Carrinho (CT-010 a CT-013)
- Pedido/Checkout (CT-014 a CT-017)
- Notificações (CT-018 a CT-020)
- Administração (CT-021 a CT-023)


## Convenção de Commits

```
feat:     nova funcionalidade
fix:      correção de bug
refactor: melhoria de código
docs:     documentação
config:   configurações
test:     testes
```

## Licença

Projeto acadêmico — Pontifícia Universidade Católica de Goiás, 2026.
