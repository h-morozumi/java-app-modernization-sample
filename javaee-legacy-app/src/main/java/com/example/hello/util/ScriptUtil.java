package com.example.hello.util;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;

/**
 * Script engine utility using Nashorn JavaScript engine.
 * Nashorn was deprecated in Java 11 and removed in Java 15 (JEP 372).
 */
public class ScriptUtil {

    private static final Logger logger = Logger.getLogger(ScriptUtil.class);

    // Use system classloader to ensure Nashorn is visible in embedded Tomcat
    private static final ScriptEngineManager manager = new ScriptEngineManager(ClassLoader.getSystemClassLoader());

    /**
     * Evaluate a JavaScript expression using Nashorn (removed in Java 15).
     */
    public static Object evaluateJs(String script) throws ScriptException {
        ScriptEngine engine = manager.getEngineByName("nashorn");
        if (engine == null) {
            // Fallback for environments without Nashorn
            engine = manager.getEngineByName("js");
        }
        if (engine == null) {
            throw new ScriptException("No JavaScript engine available");
        }

        logger.info("Evaluating JavaScript: " + script);
        return engine.eval(script);
    }

    /**
     * Evaluate a math expression via JavaScript.
     */
    public static double evaluateMath(String expression) throws ScriptException {
        Object result = evaluateJs(expression);
        if (result instanceof Number) {
            return ((Number) result).doubleValue();
        }
        throw new ScriptException("Result is not a number: " + result);
    }

    /**
     * Call a JavaScript function.
     */
    public static Object callJsFunction(String functionDef, String functionName, Object... args)
            throws ScriptException, NoSuchMethodException {

        ScriptEngine engine = manager.getEngineByName("nashorn");
        if (engine == null) {
            throw new ScriptException("Nashorn engine not available");
        }

        engine.eval(functionDef);
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(functionName, args);
    }
}
