package com.pocketeasy.pocketeasy.service;

import com.pocketeasy.pocketeasy.domain.NetworkCompany;
import com.pocketeasy.pocketeasy.domain.Subscription;
import com.pocketeasy.pocketeasy.model.SubscriptionDTO;
import com.pocketeasy.pocketeasy.repos.NetworkCompanyRepository;
import com.pocketeasy.pocketeasy.repos.SubscriptionRepository;
import com.pocketeasy.pocketeasy.util.NotFoundException;
import com.pocketeasy.pocketeasy.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final NetworkCompanyRepository networkCompanyRepository;

    public SubscriptionService(final SubscriptionRepository subscriptionRepository,
            final NetworkCompanyRepository networkCompanyRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.networkCompanyRepository = networkCompanyRepository;
    }

    public List<SubscriptionDTO> findAll() {
        final List<Subscription> subscriptions = subscriptionRepository.findAll(Sort.by("id"));
        return subscriptions.stream()
                .map(subscription -> mapToDTO(subscription, new SubscriptionDTO()))
                .toList();
    }

    public SubscriptionDTO get(final UUID id) {
        return subscriptionRepository.findById(id)
                .map(subscription -> mapToDTO(subscription, new SubscriptionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final SubscriptionDTO subscriptionDTO) {
        final Subscription subscription = new Subscription();
        mapToEntity(subscriptionDTO, subscription);
        return subscriptionRepository.save(subscription).getId();
    }

    public void update(final UUID id, final SubscriptionDTO subscriptionDTO) {
        final Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(subscriptionDTO, subscription);
        subscriptionRepository.save(subscription);
    }

    public void delete(final UUID id) {
        final Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        networkCompanyRepository.findAllBySubscriptions(subscription)
                .forEach(networkCompany -> networkCompany.getSubscriptions().remove(subscription));
        subscriptionRepository.delete(subscription);
    }

    private SubscriptionDTO mapToDTO(final Subscription subscription,
            final SubscriptionDTO subscriptionDTO) {
        subscriptionDTO.setId(subscription.getId());
        subscriptionDTO.setSubscriptionName(subscription.getSubscriptionName());
        subscriptionDTO.setSubscriptionDetails(subscription.getSubscriptionDetails());
        subscriptionDTO.setActive(subscription.getActive());
        return subscriptionDTO;
    }

    private Subscription mapToEntity(final SubscriptionDTO subscriptionDTO,
            final Subscription subscription) {
        subscription.setSubscriptionName(subscriptionDTO.getSubscriptionName());
        subscription.setSubscriptionDetails(subscriptionDTO.getSubscriptionDetails());
        subscription.setActive(subscriptionDTO.getActive());
        return subscription;
    }

    public String getReferencedWarning(final UUID id) {
        final Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final NetworkCompany subscriptionsNetworkCompany = networkCompanyRepository.findFirstBySubscriptions(subscription);
        if (subscriptionsNetworkCompany != null) {
            return WebUtils.getMessage("subscription.networkCompany.subscriptions.referenced", subscriptionsNetworkCompany.getId());
        }
        return null;
    }

}
