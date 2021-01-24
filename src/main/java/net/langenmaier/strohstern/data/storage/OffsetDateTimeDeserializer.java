package net.langenmaier.strohstern.data.storage;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
    private DateTimeFormatter formatter;
    
    public OffsetDateTimeDeserializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public OffsetDateTimeDeserializer() {
        String esBasicDateTimePattern = "yyyyMMdd'T'HHmmss.SSSZ";
        this.formatter = DateTimeFormatter.ofPattern(esBasicDateTimePattern);
    }
    
    @Override
    public OffsetDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        return OffsetDateTime.parse(parser.getText(), this.formatter);
    }
}