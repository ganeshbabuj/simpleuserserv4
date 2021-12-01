package com.example.simpleuserserv4.repository;

import com.example.simpleuserserv4.entity.GroupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
    GroupEntity findByName(String name);
}
