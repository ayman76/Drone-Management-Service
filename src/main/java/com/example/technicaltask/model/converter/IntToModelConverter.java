package com.example.technicaltask.model.converter;

import com.example.technicaltask.model.Model;
import org.modelmapper.AbstractConverter;

//Class Responsible for converting integer values to Model Enum
public class IntToModelConverter extends AbstractConverter<Integer, Model> {

    @Override
    protected Model convert(Integer source) {
        if (source == null) {
            return null;
        }

        // Handle converting int to enum
        return Model.values()[source];
    }
}
