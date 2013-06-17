package com.nitorcreations.wicket.model;

import static java.util.Arrays.asList;

import java.io.Serializable;
import java.util.ArrayList;
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
    private final IModel<String> format;
    private final List<IModel<? extends Serializable>> parameters;

    public StringFormatModel(String format, Serializable... parameters) {
        this.format = new Model<String>(format);
        this.parameters = new ArrayList<IModel<? extends Serializable>>();
        for (final Serializable s : parameters) {
            this.parameters.add(new Model<Serializable>(s));
        }
    }

    public StringFormatModel(String format, IModel<? extends Serializable>... parameters) {
        this(new Model<String>(format), parameters);
    }

    public StringFormatModel(IModel<String> format, IModel<? extends Serializable>... parameters) {
        this.format = format;
        this.parameters = asList(parameters);
    }

    @Override
    public void detach() {
        format.detach();
        for (IModel<?> model : parameters) {
            model.detach();
        }
    }

    @Override
    public String getObject() {
        return String.format(getFormat(), getParameters());
    }

    private String getFormat() {
        return format.getObject();
    }

    private Object[] getParameters() {
        final Object[] obj = new Object[parameters.size()];
        int i = 0;
        for (final IModel<? extends Serializable> mod : parameters) {
            obj[i++] = mod.getObject();
        }
        return obj;
    }

    @Override
    public void setObject(String object) {
        format.setObject(object);
    }
}
