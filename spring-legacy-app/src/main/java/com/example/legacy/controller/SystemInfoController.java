package com.example.legacy.controller;

import com.example.legacy.model.User;
import com.example.legacy.model.UserForm;
import com.example.legacy.service.UserService;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * System info page demonstrating Spring Boot 2 → 3 migration issues.
 */
@Controller
public class SystemInfoController {

    private final UserService userService;

    public SystemInfoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sysinfo")
    public String sysinfo(HttpServletRequest request, Model model) {
        // Runtime Info
        String javaVersion = System.getProperty("java.version");
        String javaVendor = System.getProperty("java.vendor");
        String osInfo = System.getProperty("os.name") + " " + System.getProperty("os.arch");
        String bootVersion = SpringBootVersion.getVersion();
        String springVersion = SpringVersion.getVersion();
        String servletApi = request.getServletContext().getServerInfo();

        String runtimeInfo = "Java:             " + javaVersion + " (" + javaVendor + ")\n"
                + "Spring Boot:      " + bootVersion + "\n"
                + "Spring Framework: " + springVersion + "\n"
                + "Server:           " + servletApi + "\n"
                + "OS:               " + osInfo;
        model.addAttribute("runtimeInfo", runtimeInfo);

        // javax.servlet demo
        model.addAttribute("servletResult", demoJavaxServlet(request));

        // javax.persistence demo
        model.addAttribute("jpaResult", demoJavaxPersistence());

        // javax.validation demo
        model.addAttribute("validationResult", demoJavaxValidation());

        // Commons Text demo (CVE-2022-42889)
        model.addAttribute("commonsTextResult", demoCommonsText());

        // Trailing slash demo
        model.addAttribute("trailingSlashResult", demoTrailingSlash(request));

        // CVE library versions
        model.addAttribute("cveLibraries", getCveLibraryVersions());

        return "sysinfo";
    }

    private String demoJavaxServlet(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(HttpServletRequest.class.getName()).append("\n");
        sb.append("Method: ").append(request.getMethod()).append("\n");
        sb.append("URI: ").append(request.getRequestURI()).append("\n");
        sb.append("Session ID: ").append(request.getSession().getId(), 0, 8).append("...\n");
        sb.append("Package: javax.servlet (→ jakarta.servlet in Boot 3)");
        return sb.toString();
    }

    private String demoJavaxPersistence() {
        StringBuilder sb = new StringBuilder();
        try {
            List<User> users = userService.findAll();
            sb.append("Entity annotations: @javax.persistence.Entity, @Table, @Id, @Column\n");
            sb.append("Users in DB: ").append(users.size()).append("\n");
            if (!users.isEmpty()) {
                sb.append("First user: ").append(users.get(0).getName())
                  .append(" (").append(users.get(0).getEmail()).append(")\n");
            }
            sb.append("Package: javax.persistence (→ jakarta.persistence in Boot 3)");
        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage());
        }
        return sb.toString();
    }

    private String demoJavaxValidation() {
        StringBuilder sb = new StringBuilder();
        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            // Validate an empty form → triggers validation errors
            UserForm emptyForm = new UserForm();
            Set<ConstraintViolation<UserForm>> violations = validator.validate(emptyForm);

            sb.append("Validator: ").append(validator.getClass().getSimpleName()).append("\n");
            sb.append("Violations on empty UserForm: ").append(violations.size()).append("\n");
            for (ConstraintViolation<UserForm> v : violations) {
                sb.append("  - ").append(v.getPropertyPath()).append(": ").append(v.getMessage()).append("\n");
            }
            sb.append("Package: javax.validation (→ jakarta.validation in Boot 3)");
            factory.close();
        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage());
        }
        return sb.toString();
    }

    private String demoCommonsText() {
        StringBuilder sb = new StringBuilder();
        try {
            // Commons Text StringSubstitutor (CVE-2022-42889 - Text4Shell)
            Map<String, String> values = new HashMap<>();
            values.put("app", "Spring Legacy App");
            values.put("version", "1.0");
            values.put("boot", SpringBootVersion.getVersion());
            StringSubstitutor sub = new StringSubstitutor(values);
            String result = sub.replace("${app} v${version} (Boot ${boot})");
            sb.append("StringSubstitutor result: ").append(result).append("\n");
            sb.append("⚠ Commons Text 1.9 is vulnerable to CVE-2022-42889 (Text4Shell)");
        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage());
        }
        return sb.toString();
    }

    private String demoTrailingSlash(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Current URL: ").append(request.getRequestURL()).append("\n");
        sb.append("In Spring Boot 2: /sysinfo/ matches /sysinfo ✔\n");
        sb.append("In Spring Boot 3: /sysinfo/ does NOT match /sysinfo ✘\n");
        sb.append("⚠ Trailing slash matching disabled by default in Boot 3");
        return sb.toString();
    }

    private List<Map<String, String>> getCveLibraryVersions() {
        List<Map<String, String>> libs = new ArrayList<>();

        libs.add(cveEntry("SnakeYAML", getLibVersion("org.yaml.snakeyaml.Yaml"),
                "CVE-2022-1471", "Unsafe deserialization"));
        libs.add(cveEntry("H2 Database", getLibVersion("org.h2.engine.Constants"),
                "CVE-2022-23221", "RCE via JDBC URL"));
        libs.add(cveEntry("Log4j 1.x", "1.2.17",
                "CVE-2021-4104", "JMSAppender deserialization"));
        libs.add(cveEntry("Commons Text", getLibVersion("org.apache.commons.text.StringSubstitutor"),
                "CVE-2022-42889", "Text4Shell - script injection"));
        libs.add(cveEntry("Gson", getLibVersion("com.google.gson.Gson"),
                "CVE-2022-25647", "Deserialization DoS"));
        libs.add(cveEntry("Apache HttpClient", "4.5.2",
                "CVE-2020-13956", "Request smuggling"));

        return libs;
    }

    private Map<String, String> cveEntry(String name, String version, String cve, String description) {
        Map<String, String> entry = new LinkedHashMap<>();
        entry.put("name", name);
        entry.put("version", version);
        entry.put("cve", cve);
        entry.put("description", description);
        return entry;
    }

    private String getLibVersion(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Package pkg = clazz.getPackage();
            String version = pkg != null ? pkg.getImplementationVersion() : null;
            return version != null ? version : "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }
}
