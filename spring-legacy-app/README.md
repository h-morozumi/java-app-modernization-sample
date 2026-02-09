# Spring Legacy App

Spring Boot 2.7.18 ベースのレガシー Web アプリケーションです。  
**GitHub Copilot を活用した Spring Boot モダナイゼーション ハンズオン** の題材として、意図的に古いライブラリや Boot 3 で削除された API を使用しています。

---

## 技術スタック

| カテゴリ | 技術 / バージョン |
|----------|------------------|
| Java | **11** (Amazon Corretto) |
| Maven | **3.8.8** |
| Spring Boot | **2.7.18** |
| Spring Framework | 5.3.x |
| Spring Security | 5.x (`WebSecurityConfigurerAdapter`) |
| Template Engine | Thymeleaf |
| Database | H2 (in-memory) |
| ORM | Spring Data JPA / Hibernate 5 |
| ポート | **8081** |

---

## プロジェクト構成

```
spring-legacy-app/
├── pom.xml
├── .sdkmanrc                          # SDKMAN 自動切替 (Java 11 / Maven 3.8.8)
└── src/main/
    ├── java/com/example/legacy/
    │   ├── SpringLegacyApplication.java   # メインクラス
    │   ├── config/
    │   │   ├── SecurityConfig.java        # ⚠ WebSecurityConfigurerAdapter
    │   │   ├── WebMvcConfig.java          # ⚠ WebMvcConfigurerAdapter
    │   │   └── LegacyLoggingAutoConfiguration.java  # ⚠ spring.factories
    │   ├── controller/
    │   │   ├── HomeController.java        # GET / – トップページ
    │   │   ├── UserController.java        # /users – Thymeleaf CRUD
    │   │   ├── UserApiController.java     # /api/users – REST API
    │   │   └── SystemInfoController.java  # /sysinfo – マイグレーション問題デモ
    │   ├── filter/
    │   │   └── RequestLoggingFilter.java  # ⚠ javax.servlet.Filter
    │   ├── model/
    │   │   ├── User.java                  # ⚠ javax.persistence / javax.validation
    │   │   └── UserForm.java              # ⚠ javax.validation
    │   ├── repository/
    │   │   └── UserRepository.java        # Spring Data JPA
    │   └── service/
    │       └── UserService.java           # ビジネスロジック
    └── resources/
        ├── application.properties         # ⚠ Boot 2 固有のプロパティ
        ├── data.sql                       # 初期データ
        ├── META-INF/
        │   └── spring.factories           # ⚠ Boot 3 で廃止
        └── templates/
            ├── index.html                 # トップページ
            ├── users.html                 # ユーザー管理
            ├── sysinfo.html               # システム情報
            └── login.html                 # ログイン画面
```

---

## 起動方法

### 前提条件

- **SDKMAN** がインストール済みであること
- Java 11 および Maven 3.8.8 がインストール済みであること

```bash
# SDKMAN で Java 11 / Maven 3.8.8 をインストール (未インストールの場合)
sdk install java 11.0.26-amzn
sdk install maven 3.8.8
```

### ビルド & 起動

```bash
cd spring-legacy-app

# SDKMAN 自動切替が有効なら .sdkmanrc で Java 11 + Maven 3.8.8 に切り替わる
# 手動で切り替える場合:
sdk use java 11.0.26-amzn
sdk use maven 3.8.8

# ビルド
mvn clean package -DskipTests

# 起動 (ポート 8081)
mvn spring-boot:run
```

起動後、ブラウザで **http://localhost:8081/** にアクセスしてください。

### エンドポイント一覧

| URL | 説明 |
|-----|------|
| `/` | トップページ（CVE 一覧・マイグレーション一覧） |
| `/users` | ユーザー管理画面（Thymeleaf CRUD） |
| `/api/users` | ユーザー REST API（JSON） |
| `/sysinfo` | システム情報 & マイグレーション問題デモ |
| `/login` | Spring Security ログイン画面 |
| `/h2-console` | H2 Database コンソール |
| `/actuator/health` | ヘルスチェック |
| `/actuator/info` | アプリ情報 |

### ログイン情報

| ユーザー | パスワード | ロール |
|---------|-----------|-------|
| user | password | USER |
| admin | admin | ADMIN |

---

## CVE 脆弱性ライブラリ一覧

このアプリはマイグレーション演習用に、**既知の脆弱性を持つライブラリ** を意図的に使用しています。

| ライブラリ | バージョン | CVE | 説明 |
|-----------|-----------|-----|------|
| SnakeYAML | 1.30 | CVE-2022-1471 | Unsafe deserialization |
| H2 Database | 2.1.210 | CVE-2022-23221 | RCE via JDBC URL |
| Log4j 1.x | 1.2.17 | CVE-2021-4104 | JMSAppender デシリアライゼーション |
| Commons Text | 1.9 | CVE-2022-42889 | Text4Shell - スクリプトインジェクション |
| Gson | 2.8.6 | CVE-2022-25647 | デシリアライゼーション DoS |
| Apache HttpClient | 4.5.2 | CVE-2020-13956 | リクエストスマグリング |

---

## Spring Boot 2 → 3 マイグレーションで問題となるポイント

`/sysinfo` ページで各問題の動作を確認できます。

### 名前空間の移行 (javax → jakarta)

| Boot 2 (現在) | Boot 3 (移行先) | 該当ファイル |
|--------------|----------------|-------------|
| `javax.servlet.*` | `jakarta.servlet.*` | RequestLoggingFilter, SystemInfoController |
| `javax.persistence.*` | `jakarta.persistence.*` | User.java |
| `javax.validation.*` | `jakarta.validation.*` | UserForm, UserController, UserApiController |

### 削除・変更された API

| API / 機能 | 変更内容 | 該当ファイル |
|-----------|---------|-------------|
| `WebSecurityConfigurerAdapter` | **削除** → `SecurityFilterChain` @Bean を使用 | SecurityConfig |
| `antMatchers()` | **削除** → `requestMatchers()` を使用 | SecurityConfig |
| `authorizeRequests()` | **非推奨** → `authorizeHttpRequests()` を使用 | SecurityConfig |
| `WebMvcConfigurerAdapter` | **削除** → `WebMvcConfigurer` インターフェースを実装 | WebMvcConfig |
| `spring.factories` | **廃止** → `AutoConfiguration.imports` を使用 | META-INF/spring.factories |
| Trailing slash マッチング | **デフォルト無効化** → `/users/` は `/users` にマッチしない | 全コントローラ |
| プロパティ名 | 変更（例: `spring.redis.*` → `spring.data.redis.*`） | application.properties |

### Java バージョン

| Boot 2 (現在) | Boot 3 (移行先) |
|--------------|----------------|
| Java 11 | Java 17+ (必須) |

---

## ハンズオンの目的

GitHub Copilot を活用して、以下のモダナイゼーションを実施します：

1. **Java バージョンアップ** — Java 11 → Java 21 への移行
2. **Spring Boot アップグレード** — 2.7.18 → 3.5.x への移行
3. **javax → Jakarta 移行** — `javax.*` から `jakarta.*` への名前空間移行
4. **Security 設定の書き換え** — `WebSecurityConfigurerAdapter` → `SecurityFilterChain`
5. **MVC 設定の書き換え** — `WebMvcConfigurerAdapter` → `WebMvcConfigurer`
6. **Auto-Configuration 登録方式の移行** — `spring.factories` → `AutoConfiguration.imports`
7. **CVE 脆弱性の修正** — 脆弱なライブラリを安全なバージョンにアップデート
8. **プロパティの移行** — 廃止プロパティを新しい名前に更新
