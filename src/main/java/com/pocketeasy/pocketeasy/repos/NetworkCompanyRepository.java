package com.pocketeasy.pocketeasy.repos;

import com.pocketeasy.pocketeasy.domain.NetworkCompany;
import com.pocketeasy.pocketeasy.domain.Subscription;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NetworkCompanyRepository extends JpaRepository<NetworkCompany, UUID> {

    boolean existsByCompanyNameIgnoreCase(String companyName);

    List<NetworkCompany> findAllBySubscriptions(Subscription subscription);

    NetworkCompany findFirstBySubscriptions(Subscription subscription);

}
