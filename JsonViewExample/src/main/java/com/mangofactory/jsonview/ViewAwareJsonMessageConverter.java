package com.mangofactory.jsonview;

import java.io.IOException;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

/**
 * Adds support for Jackson's JsonView on methods
 * annotated with a {@link ResponseView} annotation
 * @author martypitt
 *
 */
public class ViewAwareJsonMessageConverter extends
        MappingJacksonHttpMessageConverter {

    public ViewAwareJsonMessageConverter()
    {
        super();
        ObjectMapper defaultMapper = new ObjectMapper();
        defaultMapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, true);
        setObjectMapper(defaultMapper);
    }
    
    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        if (object instanceof DataView && ((DataView) object).hasView())
        {
            writeView((DataView) object, outputMessage);
        } else {
            super.writeInternal(object, outputMessage);
        }
    }
    protected void writeView(DataView view, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        ObjectMapper mapper = getMapperForView(view.getView());
        JsonGenerator jsonGenerator =
                mapper.getJsonFactory().createJsonGenerator(outputMessage.getBody(), encoding);
        try {
            mapper.writeValue(jsonGenerator, view.getData());
        }
        catch (IOException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    private ObjectMapper getMapperForView(Class<?> view) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
        mapper.setSerializationConfig(mapper.getSerializationConfig().withView(view));
        return mapper;
    }
    
}
