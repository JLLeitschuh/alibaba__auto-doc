package com.alibaba.auto.doc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.auto.doc.enums.JsonSchemaTypeEnum;
import com.alibaba.auto.doc.model.json.JsonSchema;
import com.alibaba.auto.doc.model.request.RequestParam;

import org.apache.commons.lang3.BooleanUtils;

/**
 * @author ：杨帆（舲扬）
 * @date ：Created in 2020/11/8 1:13 下午
 * @description：
 */
public class JsonSchemaUtil {

    /**
     * to JsonSchema
     * @param requestParam
     * @return
     */
    public static JsonSchema buildJsonSchema(RequestParam requestParam) {
        if(requestParam == null) {
            return null;
        }

        JsonSchema jsonSchema = new JsonSchema();

        jsonSchema.setTitle(requestParam.getName());
        jsonSchema.setType(JsonSchemaTypeEnum.getByJavaType(requestParam).getValue());
        jsonSchema.setDescription(requestParam.getComment());
        if(requestParam.getEnum()) {
            jsonSchema.setEnums(requestParam.getEnumType().getValues());
        }

        List<RequestParam> childs = requestParam.getChilds();
        if (childs != null) {
            Map<String, JsonSchema> properties = new HashMap<>();
            List<String> required = new ArrayList<>();
            for (RequestParam child : requestParam.getChilds()) {
                properties.put(child.getName(), buildJsonSchema(child));
                if (BooleanUtils.isTrue(child.getRequired())) {
                    required.add(child.getName());
                }
            }

            if(requestParam.getCollection()) {
                JsonSchema childJsonSchema = new JsonSchema();
                childJsonSchema.setProperties(properties);
                if(required.size() > 0) {
                    childJsonSchema.setRequired(required);
                }
                jsonSchema.setItems(childJsonSchema);
            } else {
                jsonSchema.setProperties(properties);
                if(required.size() > 0) {
                    jsonSchema.setRequired(required);
                }
            }
        }
        return jsonSchema;
    }

}