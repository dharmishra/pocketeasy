package com.pocketeasy.pocketeasy.service;

import com.pocketeasy.pocketeasy.domain.NetworkCompany;
import com.pocketeasy.pocketeasy.domain.NetworkUsers;
import com.pocketeasy.pocketeasy.domain.Subscription;
import com.pocketeasy.pocketeasy.model.NetworkCompanyDTO;
import com.pocketeasy.pocketeasy.repos.NetworkCompanyRepository;
import com.pocketeasy.pocketeasy.repos.NetworkUsersRepository;
import com.pocketeasy.pocketeasy.repos.SubscriptionRepository;
import com.pocketeasy.pocketeasy.util.NotFoundException;
import com.pocketeasy.pocketeasy.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class NetworkCompanyService {

    private final NetworkCompanyRepository networkCompanyRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NetworkUsersRepository networkUsersRepository;

    public NetworkCompanyService(final NetworkCompanyRepository networkCompanyRepository,
            final SubscriptionRepository subscriptionRepository,
            final NetworkUsersRepository networkUsersRepository) {
        this.networkCompanyRepository = networkCompanyRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.networkUsersRepository = networkUsersRepository;
    }

    public List<NetworkCompanyDTO> findAll() {
        final List<NetworkCompany> networkCompanys = networkCompanyRepository.findAll(Sort.by("id"));
        return networkCompanys.stream()
                .map(networkCompany -> mapToDTO(networkCompany, new NetworkCompanyDTO()))
                .toList();
    }

    public NetworkCompanyDTO get(final UUID id) {
        return networkCompanyRepository.findById(id)
                .map(networkCompany -> mapToDTO(networkCompany, new NetworkCompanyDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final NetworkCompanyDTO networkCompanyDTO) {
        final NetworkCompany networkCompany = new NetworkCompany();
        mapToEntity(networkCompanyDTO, networkCompany);
        return networkCompanyRepository.save(networkCompany).getId();
    }

    public void update(final UUID id, final NetworkCompanyDTO networkCompanyDTO) {
        final NetworkCompany networkCompany = networkCompanyRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(networkCompanyDTO, networkCompany);
        networkCompanyRepository.save(networkCompany);
    }

    public void delete(final UUID id) {
        networkCompanyRepository.deleteById(id);
    }

    private NetworkCompanyDTO mapToDTO(final NetworkCompany networkCompany,
            final NetworkCompanyDTO networkCompanyDTO) {
        networkCompanyDTO.setId(networkCompany.getId());
        networkCompanyDTO.setCompanyName(networkCompany.getCompanyName());
        networkCompanyDTO.setDescription(networkCompany.getDescription());
        networkCompanyDTO.setActive(networkCompany.getActive());
        networkCompanyDTO.setSubscriptions(networkCompany.getSubscriptions().stream()
                .map(subscription -> subscription.getId())
                .toList());
        return networkCompanyDTO;
    }

    private NetworkCompany mapToEntity(final NetworkCompanyDTO networkCompanyDTO,
            final NetworkCompany networkCompany) {
        networkCompany.setCompanyName(networkCompanyDTO.getCompanyName());
        networkCompany.setDescription(networkCompanyDTO.getDescription());
        networkCompany.setActive(networkCompanyDTO.getActive());
        final List<Subscription> subscriptions = subscriptionRepository.findAllById(
                networkCompanyDTO.getSubscriptions() == null ? Collections.emptyList() : networkCompanyDTO.getSubscriptions());
        if (subscriptions.size() != (networkCompanyDTO.getSubscriptions() == null ? 0 : networkCompanyDTO.getSubscriptions().size())) {
            throw new NotFoundException("one of subscriptions not found");
        }
        networkCompany.setSubscriptions(subscriptions.stream().collect(Collectors.toSet()));
        return networkCompany;
    }

    public boolean companyNameExists(final String companyName) {
        return networkCompanyRepository.existsByCompanyNameIgnoreCase(companyName);
    }

    public String getReferencedWarning(final UUID id) {
        final NetworkCompany networkCompany = networkCompanyRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final NetworkUsers networkCompanyIdNetworkUsers = networkUsersRepository.findFirstByNetworkCompanyId(networkCompany);
        if (networkCompanyIdNetworkUsers != null) {
            return WebUtils.getMessage("networkCompany.networkUsers.networkCompanyId.referenced", networkCompanyIdNetworkUsers.getId());
        }
        return null;
    }

}
