package io.github.charlesvall.mobamatch.infrastructure.adapter.repository.specification;

import io.github.charlesvall.mobamatch.domain.model.MatchSearchCriteria;
import io.github.charlesvall.mobamatch.infrastructure.entity.MatchEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchSpecificationBuilder {
    public Specification<MatchEntity> build(MatchSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();


            if (criteria.getRegion() != null && !criteria.getRegion().toString().isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("region"), criteria.getRegion())
                );
            }

            if (criteria.getMinAverageLevel() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("averageSkill"), criteria.getMinAverageLevel()
                        )
                );
            }

            if (criteria.getMaxAverageLevel() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("averageSkill"), criteria.getMaxAverageLevel()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}