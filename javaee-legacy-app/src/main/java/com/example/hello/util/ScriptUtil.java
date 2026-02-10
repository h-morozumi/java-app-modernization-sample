package com.example.hello.util;

import javax.script.ScriptException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

/**
 * Script engine utility using GraalVM JavaScript (replacement for Nashorn).
 * Nashorn was deprecated in Java 11 and removed in Java 15 (JEP 372).
 * Migrated to GraalVM Polyglot API for JavaScript execution.
 */
public class ScriptUtil {

    private static final Logger logger = LogManager.getLogger(ScriptUtil.class);

    /**
     * Evaluate a JavaScript expression using GraalVM JavaScript.
     */
    public static Object evaluateJs(String script) throws ScriptException {
        try (Context context = Context.newBuilder("js")
                .allowAllAccess(false)
                .build()) {
            logger.info("Evaluating JavaScript: " + script);
            Value result = context.eval("js", script);
            
            if (result.isNumber()) {
                return result.asDouble();
            } else if (result.isString()) {
                return result.asString();
            } else if (result.isBoolean()) {
                return result.asBoolean();
            } else {
                return result.toString();
            }
        } catch (Exception e) {
            throw new ScriptException("JavaScript evaluation failed: " + e.getMessage());
        }
    }

    /**
     * Evaluate a math expression via JavaScript.
     */
    public static double evaluateMath(String expression) throws ScriptException {
        Object result = evaluateJs(expression);
        if (result instanceof Number number) {
            return number.doubleValue();
        }
        throw new ScriptException("Result is not a number: " + result);
    }

    /**
     * Call a JavaScript function.
     */
    public static Object callJsFunction(String functionDef, String functionName, Object... args)
            throws ScriptException {
        try (Context context = Context.newBuilder("js")
                .allowAllAccess(false)
                .build()) {
            
            // Define the function
            context.eval("js", functionDef);
            
            // Get the function and call it
            Value function = context.getBindings("js").getMember(functionName);
            if (function == null || !function.canExecute()) {
                throw new ScriptException("Function not found or not executable: " + functionName);
            }
            
            Value result = function.execute(args);
            
            if (result.isNumber()) {
                return result.asDouble();
            } else if (result.isString()) {
                return result.asString();
            } else if (result.isBoolean()) {
                return result.asBoolean();
            } else {
                return result.toString();
            }
        } catch (Exception e) {
            throw new ScriptException("JavaScript function call failed: " + e.getMessage());
        }
    }
}
