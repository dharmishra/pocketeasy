package com.pocketeasy.pocketeasy.repos;

import com.pocketeasy.pocketeasy.domain.NetworkCompany;
import com.pocketeasy.pocketeasy.domain.NetworkUsers;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NetworkUsersRepository extends JpaRepository<NetworkUsers, UUID> {

    NetworkUsers findFirstByNetworkCompanyId(NetworkCompany networkCompany);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPasswordIgnoreCase(String password);

}
