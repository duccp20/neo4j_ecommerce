package com.neo4j_ecom.demo.model.entity.Specfication;

import com.neo4j_ecom.demo.utils.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecificationOption {

    private ProductType name;

    private String value;

}
