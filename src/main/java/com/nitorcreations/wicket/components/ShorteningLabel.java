package com.nitorcreations.wicket.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * A label that shortens its shown value and adds an ellipsis if the text is shortened.
 */
public class ShorteningLabel extends Label {
    private static final long serialVersionUID = 7173408262437724898L;

    private static final int DEFAULT_SIZE = 20;

    private final int maxLength;

    public ShorteningLabel(String id) {
        this(id, DEFAULT_SIZE);
    }

    public ShorteningLabel(String id, IModel<?> model) {
        this(id, model, DEFAULT_SIZE);
    }

    public ShorteningLabel(String id, int maxLength) {
        super(id);
        this.maxLength = maxLength;
    }

    public ShorteningLabel(String id, IModel<?> model, int maxLength) {
        super(id, model);
        this.maxLength = maxLength;
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        tag.put("title", getDefaultModelObjectAsString());
    }

    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        replaceComponentTagBody(markupStream, openTag, getShortenedString());
    }

    private String getShortenedString() {
        final String fullValue = getDefaultModelObjectAsString();
        if (fullValue.length() > maxLength) {
            StringBuilder sb = new StringBuilder();
            sb.append(fullValue.substring(0, maxLength));
            sb.append('…');
            return sb.toString();
        }
        return fullValue;
    }

    public int getMaxLength() {
        return maxLength;
    }
}
