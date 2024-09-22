//package com.neo4j_ecom.demo.config;
//
//import com.neo4j_ecom.demo.validator.ValidateQueryParams;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class QueryParamInterceptor implements HandlerInterceptor {
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            if (handlerMethod.getMethod().isAnnotationPresent(ValidateQueryParams.class)) {
//                String page = request.getParameter("page");
//                String sortBy = request.getParameter("sortBy");
//                String sortOrder = request.getParameter("sortOrder");
//
//                // Validate page
//                if (page != null) {
//                    try {
//                        int pageNum = Integer.parseInt(page);
//                        if (pageNum < 0) {
//                            sendErrorResponse(response, "Invalid page parameter. Must be a non-negative number.");
//                            return false;
//                        }
//                    } catch (NumberFormatException e) {
//                        sendErrorResponse(response, "Invalid page parameter. Must be a number.");
//                        return false;
//                    }
//                }
//
//                // Validate sortBy
//                List<String> allowedSortFields = Arrays.asList("updatedAt", "createdAt", "rating");
//                if (sortBy != null && !allowedSortFields.contains(sortBy)) {
//                    sendErrorResponse(response, "Invalid sortBy parameter.");
//                    return false;
//                }
//
//                // Validate sortOrder
//                List<String> allowedSortOrders = Arrays.asList("ASC", "DESC");
//                if (sortOrder != null && !allowedSortOrders.contains(sortOrder.toUpperCase())) {
//                    sendErrorResponse(response, "Invalid sortOrder parameter. Must be 'ASC' or 'DESC'.");
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    private void sendErrorResponse(HttpServletResponse response, String message) throws Exception {
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        response.setContentType("application/json");
//        response.getWriter().write("{\"error\": \"" + message + "\"}");
//    }
//
//}