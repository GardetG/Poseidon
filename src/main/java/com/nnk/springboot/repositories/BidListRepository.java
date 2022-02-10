package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.BidList;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for BidList entity.
 */
public interface BidListRepository extends JpaRepository<BidList, Integer> {

}
