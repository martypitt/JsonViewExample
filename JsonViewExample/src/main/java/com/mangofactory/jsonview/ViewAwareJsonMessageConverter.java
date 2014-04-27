package com.mangofactory.jsonview;

import java.io.IOException;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Adds support for Jackson's JsonView on methods
 * annotated with a {@link ResponseView} annotation
 * @author martypitt
 *
 */
public class ViewAwareJsonMessageConverter extends
        MappingJackson2HttpMessageConverter {

    public ViewAwareJsonMessageConverter()
    {
        super();
        ObjectMapper defaultMapper = new ObjectMapper();
        defaultMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
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
        ObjectWriter writer = getWriterForView(view.getView());
        JsonGenerator jsonGenerator = 
                writer.getFactory().createGenerator(outputMessage.getBody(), encoding);
        try {
            writer.writeValue(jsonGenerator, view.getData());
        }
        catch (IOException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    private ObjectWriter getWriterForView(Class<?> view) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        return mapper.writer().withView(view);
    }
    
}
