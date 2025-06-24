package cn.sunyc.ddnsgeneral.utils;

import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.TextStringBuilder;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 字符串模板替换工具
 *
 * @author sun yu chao
 */
public enum YcStringTemplateUtil {
    /**
     * curly brackets, like ${}
     */
    BRACES('\\', "${", "}"),
    /**
     * square brackets, like $[]
     */
    BRACKETS('\\', "$[", "]");

    private final char escape;
    private final String prefix;
    private final String suffix;

    YcStringTemplateUtil(char escape, String prefix, String suffix) {
        this.escape = escape;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * 执行变量替换。
     *
     * @param template 结果模板
     * @param params   替换参数
     * @return 替换后结果
     */
    public String replace(String template, Map<String, Object> params) {
        if (template == null || params == null) {
            return template;
        }
        final HashMap<String, Object> vars = new HashMap<>(generateDefaultVars());
        vars.putAll(params);
        // StringSubstitutor非线程安全，使用时候再new
        final StringSubstitutor substitutor = new ScriptStringSubstitutor(vars, prefix, suffix, escape);
        return substitutor.replace(template);
    }

    /**
     * 生成默认自带的参数
     */
    private static Map<String, Object> generateDefaultVars() {
        final Map<String, Object> vars = new HashMap<>();

        final LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        vars.put("year", now.format(DateTimeFormatter.ofPattern("yyyy")));
        vars.put("month", now.format(DateTimeFormatter.ofPattern("MM")));
        vars.put("day", now.format(DateTimeFormatter.ofPattern("dd")));
        vars.put("CDATA_START", "<![CDATA[");
        vars.put("CDATA_END", "]]>");

        return vars;
    }


    /**
     * 接入了脚本引擎的字符串替换器
     */
    private static class ScriptStringSubstitutor extends StringSubstitutor {

        private final Map<String, Object> params;
        /**
         * 脚本引擎,线程不安全
         */
        private static final ThreadLocal<ScriptEngine> localScriptEngine = ThreadLocal.withInitial(() ->
                new ScriptEngineManager().getEngineByName("groovy"));


        public ScriptStringSubstitutor(Map<String, Object> params, String prefix, String suffix, char escape) {
            super(params, prefix, suffix, escape);
            this.params = params;
        }

        @Override
        protected String resolveVariable(String variableName, TextStringBuilder buf, int startPos, int endPos) {
            // 获取原始变量值
            final Object value = params.get(variableName);
            if (value != null) {
                return String.valueOf(value);
            }
            // 尝试解析表达式
            try {
                final ScriptEngine scriptEngine = localScriptEngine.get();
                // 清空参数
                scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).clear();
                // 设置参数
                params.forEach(scriptEngine::put);
                final Object result = scriptEngine.eval(variableName);
                return result == null ? null : result.toString();
            } catch (ScriptException e) {
                System.out.println("[AIX_SCRIPT_ANALYSIS] script analysis fail.");
                return null;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(BRACKETS.replace("hello $[name] $[year] $[month] $[day]", new HashMap<>()));
    }
}
