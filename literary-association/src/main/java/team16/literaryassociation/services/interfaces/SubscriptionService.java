package team16.literaryassociation.services.interfaces;

import org.springframework.http.ResponseEntity;
import team16.literaryassociation.dto.BillingPlanDTO;
import team16.literaryassociation.dto.SubscriptionHistoryDTO;
import team16.literaryassociation.dto.SubscriptionRequestDTO;

import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.model.Subscription;

import java.util.List;

public interface SubscriptionService {

    List<BillingPlanDTO> getMerchantBillingPlans(String merchantEmail);
    ResponseEntity<?> sendSubscriptionToPC(SubscriptionRequestDTO dto, Reader reader, Merchant merchant);
    Subscription save(Subscription subscription);
    Boolean readerHasSubscription(Reader reader, Merchant merchant);
    List<SubscriptionHistoryDTO> getSubscriptions();

}
