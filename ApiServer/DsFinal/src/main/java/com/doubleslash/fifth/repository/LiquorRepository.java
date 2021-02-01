package com.doubleslash.fifth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doubleslash.fifth.dto.LiquorDTO;
import com.doubleslash.fifth.vo.LiquorVO;

@Repository
public interface LiquorRepository extends JpaRepository<LiquorVO, Integer> {

}
