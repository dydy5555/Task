package org.example.task.repository.users;


import org.example.task.common.Provider;
import org.example.task.model.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByPhoneNumberAndProvider(String phoneNumber, Provider provider);
}
