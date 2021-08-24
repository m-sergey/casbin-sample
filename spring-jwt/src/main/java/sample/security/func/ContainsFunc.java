package sample.security.func;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.List;
import java.util.Map;

public class ContainsFunc extends AbstractFunction {

    public ContainsFunc() {}

    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        String value = FunctionUtils.getStringValue(arg1, env);
        List<String> collection = (List<String>) FunctionUtils.getJavaObject(arg2, env);
        return AviatorBoolean.valueOf(isInCollection(value, collection));
    }

    private boolean isInCollection(String value, List<String> collection) {
        return collection.contains(value);
    }

    public String getName() {
        return getNameStatic();
    }

    public static String getNameStatic() {
        return "contains";
    }
}
