package com.memsource.backend.memsource.repositories;

import com.memsource.backend.memsource.data.UserConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConfigurationRepo extends JpaRepository<UserConfigurationEntity, Long> {
}
