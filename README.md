# Java App Modernization ハンズオン

**GitHub Copilot App Modernization** を活用して、2 つの Java レガシーアプリケーションを最新の Java 21 + Azure 対応にマイグレーションするハンズオンです。

---

## 概要

このリポジトリには、異なる世代のレガシー Java アプリケーションが 2 つ含まれています。  
**GitHub Copilot App Modernization for Java** (VS Code 拡張機能) を使い、AI 支援でコードのアップグレード・Azure 移行を体験します。

| アプリ | 現在の技術スタック | マイグレーション先 |
|--------|-------------------|-------------------|
| **javaee-legacy-app** | Java 8 + Java EE 7 (Servlet/JSP) | Java 21 + Jakarta EE 10 |
| **spring-legacy-app** | Java 11 + Spring Boot 2.7.18 | Java 21 + Spring Boot 3.5 |

---

## 前提条件

### 必須ツール

| ツール | 用途 |
|--------|------|
| **VS Code** (1.101 以降) | 開発エディタ |
| **GitHub Copilot** (有効なサブスクリプション) | AI コーディング支援 |
| **GitHub Copilot App Modernization** 拡張機能 | Java マイグレーション支援 |
| **Java 21** (Microsoft Build of OpenJDK 推奨) | ターゲット JDK / ツール実行環境 |
| **Maven** | ビルドツール |

### Dev Container を使用する場合 (推奨)

このリポジトリには Dev Container 設定が含まれています。VS Code の **Dev Containers** 拡張機能を使うと、必要な SDK がすべて自動インストールされます。

### マルチルートワークスペースの使用 (推奨)

このリポジトリは **マルチルートワークスペース** として構成されています。`java-app-modernization.code-workspace` を開くと、各アプリケーションが独立したフォルダとして認識され、以下のメリットがあります：

- 各アプリごとに個別のビルド設定・デバッグ設定が適用される
- GitHub Copilot App Modernization が各アプリを独立したプロジェクトとして認識
- フォルダごとに適切な Java/Maven バージョンが自動切替

**ワークスペースを開く方法:**

```bash
# VS Code でワークスペースファイルを直接開く
code java-app-modernization.code-workspace
```

または、VS Code メニューから **ファイル** → **ファイルでワークスペースを開く** を選択し、`java-app-modernization.code-workspace` を選択してください。

**ワークスペース構成:**

| フォルダ名 | パス | 説明 |
|-----------|------|------|
| root | `.` | プロジェクトルート (共通設定・ドキュメント) |
| javaee-legacy-app (Java 8 → 21) | `javaee-legacy-app/` | Java EE レガシーアプリ |
| spring-legacy-app (Boot 2 → 3) | `spring-legacy-app/` | Spring Boot レガシーアプリ |

```bash
# Dev Container の起動後、SDKMAN で以下が自動インストールされます:
# Java:  8.0.472-amzn / 11.0.26-amzn / 21.0.9-amzn
# Maven: 3.6.3 / 3.8.8 / 3.9.12
```

各ディレクトリに `.sdkmanrc` が配置されており、`cd` するだけで適切な Java/Maven バージョンに自動切替されます。

| ディレクトリ | Java | Maven | 用途 |
|------------|------|-------|------|
| `/` (プロジェクトルート) | 21.0.9-amzn | 3.9.12 | ツール実行 / マイグレーション先 |
| `javaee-legacy-app/` | 8.0.472-amzn | 3.6.3 | レガシーアプリ① |
| `spring-legacy-app/` | 11.0.26-amzn | 3.8.8 | レガシーアプリ② |

---

## リポジトリ構成

```
java-app-modernization-sample/
├── .sdkmanrc                          # Java 21 / Maven 3.9.12 自動切替 (ツール実行用)
├── java-app-modernization.code-workspace  # マルチルートワークスペース設定
├── README.md                          # ← このファイル (ハンズオン手順)
├── .devcontainer/
│   ├── devcontainer.json              # Dev Container 設定
│   └── post-create.sh                 # SDKMAN + SDK 自動インストール
├── javaee-legacy-app/                 # 🔴 レガシーアプリ① (Java 8 / Java EE 7)
│   ├── .sdkmanrc                      #    Java 8 / Maven 3.6.3 自動切替
│   ├── pom.xml
│   ├── README.md                      #    アプリ詳細説明
│   └── src/
└── spring-legacy-app/                 # 🔴 レガシーアプリ② (Java 11 / Spring Boot 2)
    ├── .sdkmanrc                      #    Java 11 / Maven 3.8.8 自動切替
    ├── pom.xml
    ├── README.md                      #    アプリ詳細説明
    └── src/
```

---

## 使用するツール

### GitHub Copilot App Modernization for Java

Microsoft が提供する VS Code 拡張機能で、以下の機能を備えています：

| 機能 | 説明 |
|------|------|
| **App Assessment** | AppCAT によるコード分析 → クラウド対応度の評価レポート生成 |
| **Code Transformation** | OpenRewrite + AI によるコード自動変換 |
| **Build & Patch** | ビルド検証 → エラー自動修正 |
| **CVE Scan** | 脆弱性の検出と修正 |
| **Unit Test Migration** | 既存テストの移行 + 新規テスト生成 |
| **Containerize & Deploy** | Dockerfile 生成 → Azure デプロイ |

### 拡張機能のインストール

VS Code で以下の拡張機能をインストールしてください：

1. **GitHub Copilot** — `GitHub.copilot`
2. **GitHub Copilot Chat** — `GitHub.copilot-chat`
3. **GitHub Copilot App Modernization** — `vscjava.migrate-java-to-azure`

---

## ハンズオン手順

### Part 1: javaee-legacy-app のマイグレーション (Java 8 → Java 21)

#### Step 1: 現状確認

```bash
cd javaee-legacy-app
mvn clean package
mvn tomcat7:run
```

ブラウザで http://localhost:8080/ にアクセスし、アプリが Java 8 で動作することを確認します。  
http://localhost:8080/sysinfo で非推奨 API の動作を確認します。

#### Step 2: App Assessment (評価)

1. VS Code サイドバーで **GitHub Copilot App Modernization** パネルを開く
2. **ASSESSMENT** セクションの **Run Assessment** をクリック
3. AppCAT がコードを分析し、以下を含む評価レポートが生成されます：
   - Java 8 → 21 で削除される API の一覧
   - CVE 脆弱性を持つライブラリの検出
   - javax → jakarta への移行が必要な箇所
   - Azure 移行に必要な変更点

#### Step 3: Code Upgrade (アップグレード)

Copilot Chat を **Agent Mode** で開き、以下のプロンプトを実行します：

```
Upgrade project to Java 21 using Java upgrade tools
```

ツールがアップグレードプランを生成します。プランの内容を確認し、**Continue** で実行します。

**主な変換内容:**

| 変換項目 | Before (Java 8) | After (Java 21) |
|---------|-----------------|-----------------|
| Base64 エンコード | `sun.misc.BASE64Encoder` | `java.util.Base64` |
| XML バインディング | `javax.xml.bind` (JAXB) | Jakarta XML Binding or 代替 |
| JavaScript エンジン | Nashorn (`ScriptEngine`) | GraalJS or 代替 |
| ラッパーコンストラクタ | `new Integer(42)` | `Integer.valueOf(42)` |
| スレッド制御 | `Thread.stop()` / `finalize()` | `Thread.interrupt()` + try-with-resources |
| セキュリティマネージャ | `SecurityManager` | 代替セキュリティ機構 |
| 名前空間 | `javax.*` | `jakarta.*` |

#### Step 4: CVE 修正

**TASKS** セクションの **Quality & Security Tasks** → **CVE Scan** を実行し、脆弱なライブラリを安全なバージョンに更新します。

| ライブラリ | 現在のバージョン | 主な CVE |
|-----------|----------------|---------|
| Log4j 1.x | 1.2.17 | CVE-2021-4104 |
| Commons Collections | 3.2.1 | CVE-2015-6420 |
| Commons FileUpload | 1.3.1 | CVE-2016-1000031 |
| Jackson Databind | 2.9.8 | CVE-2019-12384 他 |
| Apache HttpClient | 4.5.2 | CVE-2020-13956 |

#### Step 5: ビルド & テスト

```bash
# Java 21 でビルドが通ることを確認
sdk use java 21.0.9-amzn
sdk use maven 3.9.12
mvn clean package
```

---

### Part 2: spring-legacy-app のマイグレーション (Spring Boot 2 → 3)

#### Step 1: 現状確認

```bash
cd spring-legacy-app
mvn clean package -DskipTests
mvn spring-boot:run
```

ブラウザで http://localhost:8081/ にアクセスし、アプリが動作することを確認します。  
http://localhost:8081/sysinfo でマイグレーション問題となるコードの動作を確認します。

#### Step 2: App Assessment (評価)

1. VS Code サイドバーで **GitHub Copilot App Modernization** パネルを開く
2. **Run Assessment** を実行
3. 評価レポートで以下が検出されます：
   - javax → jakarta の移行が必要な箇所
   - Spring Security の非推奨 API
   - spring.factories の廃止
   - CVE 脆弱性ライブラリ

#### Step 3: Code Upgrade (アップグレード)

Agent Mode で以下のプロンプトを実行します：

```
Upgrade project to Java 21 and Spring Boot 3.5 using Java upgrade tools
```

**主な変換内容:**

| カテゴリ | Before (Boot 2) | After (Boot 3) |
|---------|-----------------|----------------|
| Java バージョン | 11 | 21 |
| Spring Boot | 2.7.18 | 3.5.x |
| 名前空間 | `javax.servlet.*` / `javax.persistence.*` / `javax.validation.*` | `jakarta.servlet.*` / `jakarta.persistence.*` / `jakarta.validation.*` |
| Security 設定 | `extends WebSecurityConfigurerAdapter` | `@Bean SecurityFilterChain` |
| URL マッチング | `antMatchers()` | `requestMatchers()` |
| 認可設定 | `authorizeRequests()` | `authorizeHttpRequests()` |
| MVC 設定 | `extends WebMvcConfigurerAdapter` | `implements WebMvcConfigurer` |
| Auto Configuration | `META-INF/spring.factories` | `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` |
| Trailing Slash | デフォルト有効 | デフォルト無効 → 明示的設定が必要 |
| プロパティ | `spring.redis.*` 等 | `spring.data.redis.*` 等 |

#### Step 4: CVE 修正

| ライブラリ | 現在のバージョン | 主な CVE |
|-----------|----------------|---------|
| SnakeYAML | 1.30 | CVE-2022-1471 |
| H2 Database | 2.1.210 | CVE-2022-23221 |
| Log4j 1.x | 1.2.17 | CVE-2021-4104 |
| Commons Text | 1.9 | CVE-2022-42889 (Text4Shell) |
| Gson | 2.8.6 | CVE-2022-25647 |

#### Step 5: ビルド & テスト

```bash
# Java 21 + Spring Boot 3 でビルドが通ることを確認
sdk use java 21.0.9-amzn
sdk use maven 3.9.12
mvn clean package
```

---

### Part 3: Azure へのデプロイ (オプション)

マイグレーション完了後、**GitHub Copilot App Modernization** の **Deploy** 機能を使って Azure にデプロイできます。

#### デプロイ先の選択肢

| Azure サービス | 適用シナリオ |
|---------------|-------------|
| **Azure App Service** | Web アプリのマネージドホスティング (Java 21 対応) |
| **Azure Container Apps** | コンテナ化されたマイクロサービス |
| **Azure Kubernetes Service** | Kubernetes ベースのオーケストレーション |

#### 手順

1. **TASKS** セクションの **Containerize & Deploy** を選択
2. Copilot が Dockerfile と Azure デプロイ用の IaC ファイルを生成
3. Azure リソースのプロビジョニングとデプロイが実行

---

## マイグレーション サマリー

### javaee-legacy-app

```
Java 8  ──────────────────────────→  Java 21
Java EE 7 (javax.*)  ─────────────→  Jakarta EE 10 (jakarta.*)
Servlet 3.1 / JSP 2.3  ───────────→  Jakarta Servlet 6.0 / JSP 4.0
Log4j 1.x (CVE)  ─────────────────→  Log4j 2.x or SLF4J + Logback
Commons Collections 3.x (CVE)  ────→  4.x
Jackson Databind 2.9 (CVE)  ───────→  2.17+
tomcat7-maven-plugin  ─────────────→  Spring Boot 化 or Tomcat 10+
```

### spring-legacy-app

```
Java 11  ─────────────────────────→  Java 21
Spring Boot 2.7.18  ──────────────→  Spring Boot 3.5.x
Spring Framework 5.3  ────────────→  Spring Framework 6.2
Spring Security 5.x  ─────────────→  Spring Security 6.x
javax.* 名前空間  ─────────────────→  jakarta.* 名前空間
spring.factories  ─────────────────→  AutoConfiguration.imports
SnakeYAML 1.30 (CVE)  ────────────→  2.x
H2 2.1.210 (CVE)  ────────────────→  2.3+
```

---

## 参考リンク

| リソース | URL |
|---------|-----|
| GitHub Copilot App Modernization 概要 | https://learn.microsoft.com/azure/developer/github-copilot-app-modernization/overview |
| Java 開発者向けガイド | https://learn.microsoft.com/azure/developer/java/migration/migrate-github-copilot-app-modernization-for-java |
| クイックスタート: Java プロジェクトのアップグレード | https://learn.microsoft.com/azure/developer/github-copilot-app-modernization/quickstart-upgrade |
| クイックスタート: 評価と移行 | https://learn.microsoft.com/azure/developer/java/migration/migrate-github-copilot-app-modernization-for-java-quickstart-assess-migrate |
| クイックスタート: Azure へのデプロイ | https://learn.microsoft.com/azure/developer/java/migration/migrate-github-copilot-app-modernization-for-java-quickstart-deploy-to-azure |
| AppCAT for Java (コマンドライン版) | https://learn.microsoft.com/azure/migrate/appcat/java-preview |
| Azure App Service Java ランタイム | https://learn.microsoft.com/azure/app-service/configure-language-java |
| VS Code 拡張機能 (Marketplace) | https://marketplace.visualstudio.com/items?itemName=vscjava.migrate-java-to-azure |

---

## ライセンス

MIT License