# blog.derecc

Api de Blog pessoal desenvolvido para publicação e gerenciamento de artigos.

O projeto possui autenticação de usuários, criação e edição de posts, upload de imagens e um painel administrativo onde cada usuário pode gerenciar apenas seus próprios artigos.

## ✨ Funcionalidades

- Cadastro e login de usuários
- Autenticação com JWT
- Perfil do usuário
- Upload e alteração de avatar
- Criação de artigos
- Edição e exclusão de artigos
- Publicação e gerenciamento de status dos posts
- Busca de artigos
- Upload de imagens
- Painel administrativo
- Cada usuário administra apenas os próprios posts
- Paginação de artigos
- URLs amigáveis através de `slug`

## 🏗️ Estrutura

O projeto é dividido em:

- **Backend:** API REST responsável pela autenticação, usuários, posts e arquivos.
- **Frontend:** Interface web do blog e painel administrativo.

## 🚀 Executando o projeto

### Backend

Entre na pasta do backend e execute:

```bash
./mvnw spring-boot:run
