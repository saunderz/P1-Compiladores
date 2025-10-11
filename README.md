# P1-Compiladores Partes 1 e 2

Projeto didático de **Tradução Dirigida por Sintaxe (SDT)** que traduz expressões aritméticas **infixadas** para **pós-fixadas** (RPN) por meio de ações impressas no console (`push`, `add`, `sub`).  
Nesta fase não usamos Maven nem `package` — tudo compila com `javac` direto.

---

## ✅ Requisitos
- **Java JDK 17+** no PATH
- (Opcional) **Git** para versionamento

Verifique no terminal:
```bash
java -version
javac -version
```

---

## 📦 Estrutura do repositório (até a Parte 2)
```
P1-Compiladores/
├─ Main.java
├─ Parser.java
├─ Lexer.java         # Parte 2
├─ Token.java         # Parte 2
└─ TokenType.java     # Parte 2
```

> **Parte 1** usa apenas `Main.java` e `Parser.java` (sem lexer).  
> **Parte 2** adiciona `Lexer.java`, `Token.java` e `TokenType.java` e altera o `Parser` para consumir tokens.

---

# 🧩 Parte 1 — Um simples tradutor (sem analisador léxico)

Traduz expressões infixadas com **dígitos únicos (0–9)** e operadores `+` / `-` para ações:
- `push <dígito>`, `add`, `sub`.

### Gramática (Parte 1)
```
expr  -> digit oper
oper  -> + digit oper
      |  - digit oper
      |  ε
digit -> 0 | … | 9
```

### Como compilar e executar (Parte 1)
```bash
javac Main.java Parser.java
java Main
```

### Saída esperada (exemplo `8+5-7+9`)
```
push 8
push 5
add
push 7
sub
push 9
add
```

### Limitações (intencionais)
- Apenas **um dígito** por número (`45` gera erro).
- Apenas `+` e `-` (sem `*`/`/`).
- Sem analisador léxico; o `Parser` lê caractere a caractere.

---

# 🔎 Parte 2 — Analisador léxico (números com vários dígitos)

Introduzimos um **lexer** (scanner) que transforma a entrada em **tokens** sob demanda:
- `NUMBER` para **[0-9]+** (um ou mais dígitos),
- `PLUS`, `MINUS`,
- `EOF`.

O `Parser` passa a consumir **tokens** do `Lexer` e mantém as ações de tradução para RPN:
- `push <n>`, `add`, `sub`.

### Gramática (Parte 2)
```
expr   -> number oper
oper   -> + number oper
       |  - number oper
       |  ε
number -> [0-9]+
```

### Como compilar e executar (Parte 2)
```bash
javac TokenType.java Token.java Lexer.java Parser.java Main.java
java Main
```

> Em `Main.java`, use uma entrada com múltiplos dígitos, por exemplo:
> ```java
> String input = "45+89-876";
> ```

### Saída esperada (exemplo `45+89-876`)
```
push 45
push 89
add
push 876
sub
```

### Casos de teste rápidos
- `0` → `push 0`  
- `10-2-3` →  
  ```
  push 10
  push 2
  sub
  push 3
  sub
  ```
- `8*9` → **erro léxico** (não tratamos `*` ainda)
- `+9` / `9+` → **erro sintático** (faltou `number`)

---

## 🧠 O que cada arquivo faz
- **Main.java**: ponto de entrada; define a string de entrada e chama `Parser.parse()`.
- **Parser.java**: analisador sintático por **descida recursiva**; Parte 1 lê chars; Parte 2 consome **tokens** do lexer e imprime as ações (`push/add/sub`).
- **Lexer.java** *(Parte 2)*: scanner **sob demanda** que agrega dígitos em `NUMBER` e reconhece `+`, `-`, `EOF`.
- **Token.java / TokenType.java** *(Parte 2)*: representação de tokens e seus tipos.

---


