# JavaEE Legacy App

Java EE 7 + Servlet 3.1 ベースのレガシー Web アプリケーションです。  
**GitHub Copilot を活用した Java モダナイゼーション ハンズオン** の題材として、意図的に古いライブラリや非推奨 API を使用しています。

---

## 技術スタック

| カテゴリ | 技術 / バージョン |
|----------|------------------|
| Java | **8** (Amazon Corretto) |
| Maven | **3.6.3** |
| Java EE API | 7.0 (`javax.*` 名前空間) |
| Servlet | 3.1 |
| JSP / JSTL | 2.3 / 1.2 |
| 組み込みサーバー | Tomcat 7 (Maven Plugin) |

---

## プロジェクト構成

```
javaee-legacy-app/
├── pom.xml
├── .sdkmanrc                          # SDKMAN 自動切替 (Java 8 / Maven 3.6.3)
└── src/main/
    ├── java/com/example/hello/
    │   ├── servlet/
    │   │   ├── HelloServlet.java      # GET /hello  – JSP フォワード
    │   │   ├── UserApiServlet.java    # GET /api/users – JSON API (Jackson)
    │   │   ├── FileUploadServlet.java # POST /upload – ファイルアップロード
    │   │   └── SystemInfoServlet.java # GET /sysinfo – 非推奨 API デモ
    │   ├── filter/
    │   │   └── RequestLoggingFilter.java  # リクエストログ (Log4j 1.x)
    │   └── util/
    │       ├── EncodingUtil.java      # sun.misc.BASE64Encoder (Java 11 で削除)
    │       ├── XmlUtil.java           # javax.xml.bind / JAXB (Java 11 で削除)
    │       ├── ScriptUtil.java        # Nashorn JavaScript エンジン (Java 15 で削除)
    │       ├── DateUtil.java          # new Integer() 等のラッパー (Java 16+ で削除)
    │       ├── ThreadUtil.java        # Thread.stop() / finalize() (Java 20+ で削除)
    │       ├── SecurityUtil.java      # SecurityManager (Java 24 で削除)
    │       ├── CollectionUtils.java   # Commons Collections 3.x
    │       └── HttpClientUtil.java    # Apache HttpClient 4.x
    ├── resources/
    │   └── log4j.properties
    └── webapp/
        ├── index.jsp                  # トップページ
        └── WEB-INF/
            ├── web.xml
            └── views/
                ├── hello.jsp
                ├── upload.jsp
                └── error.jsp
```

---

## 起動方法

### 前提条件

- **SDKMAN** がインストール済みであること
- Java 8 および Maven 3.6.3 がインストール済みであること

```bash
# SDKMAN で Java 8 / Maven 3.6.3 をインストール (未インストールの場合)
sdk install java 8.0.472-amzn
sdk install maven 3.6.3
```

### ビルド & 起動

```bash
cd javaee-legacy-app

# SDKMAN 自動切替が有効なら .sdkmanrc で Java 8 + Maven 3.6.3 に切り替わる
# 手動で切り替える場合:
sdk use java 8.0.472-amzn
sdk use maven 3.6.3

# ビルド
mvn clean package

# 組み込み Tomcat 7 で起動 (ポート 8080)
mvn tomcat7:run
```

起動後、ブラウザで **http://localhost:8080/** にアクセスしてください。

### エンドポイント一覧

| URL | 説明 |
|-----|------|
| `/` | トップページ（CVE 一覧・非推奨 API 一覧） |
| `/hello?name=World` | Hello サーブレット（JSP フォワード） |
| `/api/users` | ユーザー JSON API（Jackson Databind） |
| `/upload` | ファイルアップロード画面（Commons FileUpload） |
| `/sysinfo` | 非推奨 API デモ（全 6 セクション） |

---

## CVE 脆弱性ライブラリ一覧

このアプリはマイグレーション演習用に、**既知の脆弱性を持つライブラリ** を意図的に使用しています。

| ライブラリ | バージョン | CVE |
|-----------|-----------|-----|
| Log4j 1.x | 1.2.17 | CVE-2021-4104 (JMSAppender デシリアライゼーション) |
| Commons Collections | 3.2.1 | CVE-2015-6420 (デシリアライゼーション RCE) |
| Commons FileUpload | 1.3.1 | CVE-2016-1000031 (RCE) |
| Commons IO | 2.4 | CVE-2021-29425 (パストラバーサル) |
| Jackson Databind | 2.9.8 | CVE-2019-12384, CVE-2019-14540 等 (デシリアライゼーション) |
| Apache HttpClient | 4.5.2 | CVE-2020-13956 (リクエストスマグリング) |

---

## Java 8 → 25 マイグレーションで問題となる API

`/sysinfo` ページで各 API の動作を確認できます。

| API / 機能 | 利用クラス | 削除バージョン | JEP |
|-----------|-----------|-------------|-----|
| `sun.misc.BASE64Encoder` | EncodingUtil | Java 11 | — |
| `javax.xml.bind` (JAXB) | XmlUtil | Java 11 | JEP 320 |
| Nashorn JavaScript エンジン | ScriptUtil | Java 15 | JEP 372 |
| `new Integer()` 等ラッパーコンストラクタ | DateUtil | Java 16+ | JEP 390 |
| `Thread.stop()` / `finalize()` | ThreadUtil | Java 20 | JEP 421 |
| `SecurityManager` | SecurityUtil | Java 24 | JEP 486 |

---

## ハンズオンの目的

GitHub Copilot を活用して、以下のモダナイゼーションを実施します：

1. **Java バージョンアップ** — Java 8 → Java 21+ への移行
2. **非推奨 API の置換** — 削除された API を最新の代替 API に移行
3. **CVE 脆弱性の修正** — 脆弱なライブラリを安全なバージョンにアップデート
4. **javax → Jakarta 移行** — Java EE (`javax.*`) から Jakarta EE (`jakarta.*`) への名前空間移行
5. **Spring Boot 化** — サーブレット / JSP ベースのアプリを Spring Boot に移行
