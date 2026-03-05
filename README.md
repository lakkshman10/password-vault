![Build](https://github.com/lakkshman10/password-vault/actions/workflows/ci.yml/badge.svg)
# 🔐 Password Vault (Java)

A secure command-line password manager built in **Java** that stores credentials using strong encryption.

This project demonstrates:

* Secure cryptography practices
* Clean layered architecture
* Automated testing
* Continuous Integration with GitHub Actions

---

# 🚀 Features

* Master password protected vault
* AES-GCM authenticated encryption
* PBKDF2 key derivation with salt
* Secure credential storage
* Command Line Interface (CLI)
* Unit testing with JUnit
* CI pipeline using GitHub Actions

---

# 🏗 Project Architecture

The project follows a **layered architecture** to separate responsibilities.

```
src
│
├── model
│   ├── Credential.java
│   └── VaultData.java
│
├── security
│   └── KeyManager.java
│
├── service
│   └── VaultService.java
│
├── storage
│   └── VaultStorage.java
│
└── Main.java
```

### Layer Responsibilities

| Layer    | Responsibility                              |
| -------- | ------------------------------------------- |
| model    | Data structures used by the application     |
| security | Cryptographic key generation and management |
| storage  | Encrypted vault file persistence            |
| service  | Business logic for managing credentials     |
| CLI      | User interaction through the command line   |

---

# 🔐 Security Design

The vault uses modern cryptographic techniques to protect stored credentials.

**Encryption Algorithm**

```
AES/GCM/NoPadding
```

**Key Derivation**

```
PBKDF2WithHmacSHA256
```

**Vault File Format**

```
[16 bytes salt][12 bytes IV][encrypted data]
```

Security properties:

* Random salt per vault
* Random IV per encryption
* Authenticated encryption (GCM tag)
* Password verification through decryption

---

# 🧪 Unit Testing

Unit tests are implemented using **JUnit 5**.

Test coverage includes:

* Adding credentials
* Searching credentials
* Deleting credentials
* Vault service functionality

---

# ⚙️ Build

The project uses **Apache Maven**.

```
mvn clean install
```

---

# ▶️ Run

After building:

```
java -cp target/password-vault.jar com.lakkshman.vault.Main
```

---

# 💻 Example CLI

```
Enter master password: ****

1. Add Credential
2. List Credentials
3. Search Credential
4. Delete Credential
5. Save & Exit
```

---

# 🛠 Tech Stack

* Java 17
* Maven
* Gson
* JUnit 5
* GitHub Actions

---

# 📄 License

MIT License
