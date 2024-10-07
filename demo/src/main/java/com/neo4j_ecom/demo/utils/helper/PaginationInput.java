package com.neo4j_ecom.demo.utils.helper;

import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import org.springframework.data.util.Pair;

public class PaginationInput {

        public static Pair<Integer, Integer> validatePaginationInput(int pageNumberInt, int pageSizeInt) {
            try {
                if (pageNumberInt < 0 || pageSizeInt < 0) {
                    throw new AppException(ErrorCode.WRONG_INPUT);
                }
                return Pair.of(pageNumberInt, pageSizeInt);
            } catch (NumberFormatException e) {
                throw new AppException(ErrorCode.WRONG_INPUT);
            }
        }

}
