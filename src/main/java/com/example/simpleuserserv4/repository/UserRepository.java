package com.example.simpleuserserv4.repository;

import com.example.simpleuserserv4.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {


    boolean existsByUsername(String username);
    UserEntity findByUsername(String username);

    //Page<UserEntity> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

    Page<UserEntity> findByFirstName(String firstName, Pageable pageable);


}
