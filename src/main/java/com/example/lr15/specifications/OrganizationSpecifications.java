package com.example.lr15.specifications;

import com.example.lr15.entities.SpisokTicket;
import org.springframework.data.jpa.domain.Specification;


public class OrganizationSpecifications {
    public static Specification<SpisokTicket> hasName(String passenger) {
        return ((root, query, criteriaBuilder) -> {
            if (passenger == null || passenger.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("passenger")), "%" + passenger.toLowerCase() + "%");
        });
    }

    public static Specification<SpisokTicket> hasRoutes(String route) {
        return ((root, query, criteriaBuilder) -> {
            if (route == null || route.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("route")), "%" + route.toLowerCase() + "%");
        });
    }
    public static Specification<SpisokTicket> hasPrice(Integer price) {
        return ((root, query, criteriaBuilder) -> {
            if (price == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price").as(Integer.class), price);
        });
    }
}
