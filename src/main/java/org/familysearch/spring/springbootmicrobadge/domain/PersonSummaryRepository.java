package org.familysearch.spring.springbootmicrobadge.domain;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

/**
 * @author danielsj
 */

@EnableScan
public interface PersonSummaryRepository extends CrudRepository<PersonSummaryEntity, String> {
}
