# P1-Compiladores — Partes 1 a 4

Projeto didático de **Tradução Dirigida por Sintaxe (SDT)** que evolui, passo a passo, de um tradutor simples para um pipeline com **scanner** e **tokens tipados**.  
Nesta fase **não foi usado Maven** nem `package` compilou-se com `javac` direto.

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

## 📦 Arquivos no repositório (até a Parte 4)
```
P1-Compiladores/
├─ Main.java
├─ Parser.java
├─ Scanner.java        # Parte 3 e 4
├─ Token.java         # Parte 2 e 4
├─ TokenType.java     # Parte 2 e 4
└─ (opcional) Lexer.java  # Parte 2
```
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
**Saída esperada** para `"8+5-7+9"`:
```
push 8
push 5
add
push 7
sub
push 9
add
```
**Limitações:** apenas **um dígito** por número e apenas `+`/`-`.  
**Sem** analisador léxico — o `Parser` lê caractere a caractere.

---

# 🔎 Parte 2 — Analisador léxico 
Introduzimos um **lexer** (scanner) que transforma a entrada em **tokens**:
- `NUMBER` para **[0-9]+** (um ou mais dígitos),
- `PLUS`, `MINUS`,
- `EOF`.

O `Parser` passa a consumir **tokens** do lexer e mantém as ações de tradução para RPN:
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
**Exemplo:** em `Main.java`:
```java
String input = "45+89-876";
```
**Saída esperada:**
```
push 45
push 89
add
push 876
sub
```

---

# 🧱 Parte 3 — Refatoração: extraindo o analisador léxico
Agora extrai-se a leitura de caracteres do `Parser` e a delega-se para um **`Scanner`** simples. Nesta etapa **cada caractere ainda é tratado como token** (apenas refatoração).

- `Scanner` fornece `nextToken()` (do tipo `char`).
- `Parser` mantém a mesma gramática da Parte 1, mas **não** tem mais `peek()` próprio.  
- `Main` permanece igual, apenas usando `Parser` com `byte[]` de entrada.

### Como compilar e executar (Parte 3)
```bash
javac Scanner.java Parser.java Main.java
java Main
```
**Saída** continua a mesma da Parte 1 para `"8+5-7+9"`.

> Dica: Se você ainda tiver os arquivos da Parte 2, **não os inclua** no comando `javac` desta parte.

---

# 🧾 Parte 4 — Suportando token `NUMBER`
Evoluímos o `Scanner` para **agrupar dígitos** e **classificar tokens** com **tipo + lexema** (`Token` + `TokenType`).  
O `Parser` agora consome `Token` (não mais `char`) e imprime as mesmas ações de tradução.

- `Scanner.nextToken()` retorna `Token`:
  - `NUMBER("289")`, `PLUS("+")`, `MINUS("-")`, `EOF("EOF")`.
- `Parser` usa `consume(TokenType)` e imprime `push <lexema>` para números.

### Como compilar e executar (Parte 4) — estado atual recomendado
```bash
javac -encoding UTF-8 TokenType.java Token.java Scanner.java Parser.java Main.java
java Main
```
**Exemplo:** em `Main.java`:
```java
String input = "289-85+0+69";
```
**Saída esperada:**
```
push 289
push 85
sub
push 0
add
push 69
add
```

---

## 🧠 O que cada arquivo faz (resumo)
- **Main.java**: ponto de entrada; define a string de entrada e chama `Parser.parse()`.
- **Parser.java**:
  - Parte 1: lê chars (sem scanner).
  - Parte 2: consome tokens do `Lexer`.
  - Parte 3: consome `char` de um `Scanner` adhoc.
  - Parte 4: consome `Token` (tipo + lexema) de `Scanner`.
- **Scanner.java**:
  - Parte 3: fornece tokens como `char`.
  - Parte 4: fornece `Token` tipado (`NUMBER`, `PLUS`, `MINUS`, `EOF`).
- **Token.java / TokenType.java**: representação dos tokens (tipo + lexema).
- **Lexer.java** (opcional / Parte 2): implementação alternativa de scanner (se usada nessa etapa).

---

