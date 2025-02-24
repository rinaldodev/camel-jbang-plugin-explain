package org.apache.camel.jbang.ai.util;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.camel.jbang.ai.SimpleRequestBuilder;
import org.apache.camel.jbang.ai.types.AlpacaRecord;
import org.apache.camel.tooling.model.BaseOptionModel;

public class CatalogUtil {
    private static final String PATTERN_FORMAT = "HH:mm:ss";

    public static void saveRecords(List<AlpacaRecord> alpacaRecords, String componentName) {
        if (!alpacaRecords.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            final File file = new File("dataset", String.format("camel-%s.json", componentName));
            file.getParentFile().mkdirs();

            try {
                mapper.writeValue(file, alpacaRecords);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String toEmbeddableText(String componentName, BaseOptionModel optionModel) {
        SimpleRequestBuilder request = new SimpleRequestBuilder();

        request.append("component", componentName)
                .append("option", optionModel.getName())
                .append("description", optionModel.getDescription())
                .append("defaultValue", optionModel.getDefaultValue())
                .append("type", optionModel.getType())
                .append("required", optionModel.isRequired())
                .append("groups", optionModel.getGroup());

        return request.build();
    }

    public static String currentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault());

        return formatter.format(Instant.now());
    }
}
