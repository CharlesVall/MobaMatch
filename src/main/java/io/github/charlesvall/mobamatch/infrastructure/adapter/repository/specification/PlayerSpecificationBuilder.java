package io.github.charlesvall.mobamatch.infrastructure.adapter.repository.specification;

import io.github.charlesvall.mobamatch.domain.model.PlayerSearchCriteria;
import io.github.charlesvall.mobamatch.infrastructure.entity.PlayerEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerSpecificationBuilder {

    public Specification<PlayerEntity> build(PlayerSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getUsername() != null && !criteria.getUsername().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("username")),
                                "%" + criteria.getUsername().toLowerCase() + "%"
                        )
                );
            }

            if (criteria.getPreferredRole() != null && !criteria.getPreferredRole().toString().isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("preferredRole"), criteria.getPreferredRole())
                );
            }

            if (criteria.getRegion() != null && !criteria.getRegion().toString().isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("region"), criteria.getRegion())
                );
            }

            if (criteria.getMinLevel() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("skillLevel"), criteria.getMinLevel()
                        )
                );
            }

            if (criteria.getMaxLevel() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("skillLevel"), criteria.getMaxLevel()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
