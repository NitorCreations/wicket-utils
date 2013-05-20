package com.nitorcreations.wicket.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Uses {@link String#format(String, Object...)} to format the output of the model.
 * 
 * @author Reko Jokelainen / Nitor Creations
 * 
 */
public class StringFormatModel implements IModel<String> {
    private static final long serialVersionUID = 1L;

    private IModel<String> format;
    private List<IModel<? extends Serializable>> parameters;

    public StringFormatModel(String format, Serializable... parameters) {
        super();
        this.format = new Model<String>(format);
        this.parameters = new ArrayList<IModel<? extends Serializable>>();
        for (final Serializable s : parameters) {
            this.parameters.add(new Model<Serializable>(s));
        }
    }

    public StringFormatModel(String format, IModel<? extends Serializable>... parameters) {
        super();
        this.format = new Model<String>(format);
        this.parameters = Arrays.asList(parameters);
    }

    public StringFormatModel(IModel<String> format, IModel<? extends Serializable>... parameters) {
        super();
        this.format = format;
        this.parameters = Arrays.asList(parameters);
    }

    @Override
    public void detach() {}

    /**
     * Returns
     */
    @Override
    public String getObject() {
        return String.format(getFormat(), getParameters());
    }

    /**
     * Get the format string.
     * 
     * @return
     */
    private String getFormat() {
        return format.getObject();
    }

    /**
     * Get the parameters.
     * 
     * @return
     */
    private Object[] getParameters() {
        final Object[] obj = new Object[parameters.size()];
        int i = 0;
        for (final IModel<? extends Serializable> mod : parameters) {
            obj[i++] = mod.getObject();
        }
        return obj;
    }

    /**
     * Sets the format string of the model.
     */
    @Override
    public void setObject(String object) {
        this.format.setObject(object);
    }

}
