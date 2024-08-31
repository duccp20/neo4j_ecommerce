package com.neo4j_ecom.demo.service.impl;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.entity.ProductDimension;
import com.neo4j_ecom.demo.service.ProductDimensionService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.Unit;
import org.springframework.stereotype.Service;

@Service
public class ProductDimensionImpl implements ProductDimensionService {


    @Override
    public ProductDimension createProductDimension(ProductDimension request) {

        ProductDimension productDimension = new ProductDimension();

        if (request.getUnitWeight() != null) {
            this.checkInvalidUnitWeight(request.getUnitWeight());
            productDimension.setUnitWeight(request.getUnitWeight());
        }

        if (request.getPackageUnit() != null) {
            this.checkInvalidUnitPackageSize(request.getPackageUnit());
            productDimension.setPackageUnit(request.getPackageUnit());
        }

        if (request.getLength() != null) {
            productDimension.setLength(request.getLength());
        }

        if (request.getWidth() != null) {
            productDimension.setWidth(request.getWidth());
        }

        if (request.getBreadth() != null) {
            productDimension.setBreadth(request.getBreadth());
        }

        if (request.getWeight() != null) {
            productDimension.setWeight(request.getWeight());
        }

        return productDimension;
    }

    @Override
    public ProductDimension updateProductDimension(ProductDimension current, ProductDimension request) {


        if (request.getLength() != null) {
            current.setLength(request.getLength());
        }

        if (request.getWidth() != null) {
            current.setWidth(request.getWidth());
        }


        if (request.getBreadth() != null) {
            current.setBreadth(request.getBreadth());
        }


        if (request.getWeight() != null) {
            current.setWeight(request.getWeight());
        }

        if (request.getUnitWeight() != null) {
            this.checkInvalidUnitWeight(request.getUnitWeight());
            current.setUnitWeight(request.getUnitWeight());
        }

        if (request.getPackageUnit() != null) {
            this.checkInvalidUnitPackageSize(request.getPackageUnit());
            current.setPackageUnit(request.getPackageUnit());
        }

        return current;
    }


    private void checkInvalidUnitWeight(Unit unit) {
        if (unit != Unit.kg && unit != Unit.pound) {
            throw new AppException(ErrorCode.INVALID_UNIT_LENGTH);
        }
    }

    private void checkInvalidUnitPackageSize(Unit unit) {
        if (unit != Unit.met && unit != Unit.cm && unit != Unit.inch) {
            throw new AppException(ErrorCode.INVALID_UNIT_PACKAGE_SIZE);
        }
    }


}
