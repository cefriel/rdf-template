package com.cefriel.template;

import com.cefriel.template.utils.TemplateFunctions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateFunctionsTest {
    @Test
    public void getMapValueTest() {
        TemplateFunctions templateFunctions = new TemplateFunctions();
        HashMap<String, String> row = new HashMap<>();
        row.put("a", "a");

        // if the key is present return the value
        Assertions.assertEquals("a", templateFunctions.getMapValue(row, "a"));
        // if key is not present and no default value has been specified return null
        Assertions.assertNull(templateFunctions.getMapValue(row, "b"));
        // if key is not present but default value has been specified return default value
        Assertions.assertEquals("default", templateFunctions.getMapValue(row, "b", "default"));
    }
    @Test
    public void splitColumnTest() {
        HashMap<String, String> row1 = new HashMap<>();
        row1.put("column", "value1 value2");
        HashMap<String, String> row2 = new HashMap<>();
        row2.put("column", "value1 value2");
        List<Map<String, String>> df;
        df = new ArrayList<>();
        df.add(row1);
        df.add(row2);

        var templateUtils = new TemplateFunctions();

        df = templateUtils.splitColumn(df, "column", " ");
        for (var row: df) {
            assert (templateUtils.checkMap(row, "column1"));
            assert (row.get("column1").equals("value1"));
            assert (templateUtils.checkMap(row, "column2"));
            assert (row.get("column2").equals("value2"));
        }
    }
}
