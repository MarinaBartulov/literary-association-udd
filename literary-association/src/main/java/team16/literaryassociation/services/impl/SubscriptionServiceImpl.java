package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import team16.literaryassociation.config.EndpointConfig;
import team16.literaryassociation.config.RestConfig;
import team16.literaryassociation.dto.*;
import team16.literaryassociation.enums.SubscriptionStatus;
import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.model.Subscription;
import team16.literaryassociation.repository.SubscriptionRepository;
import team16.literaryassociation.services.interfaces.SubscriptionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig configuration;


    @Override
    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public List<BillingPlanDTO> getMerchantBillingPlans(String merchantEmail) {

        System.out.println("Usao u get merchant billing plans servisnu metodu, salje zahtev psp-u");
        List<BillingPlanDTO> billingPlanDTOS = new ArrayList<>();
        HttpEntity<String> request = new HttpEntity<>(merchantEmail);
        ParameterizedTypeReference<List<BillingPlanDTO>> responseType = new ParameterizedTypeReference<List<BillingPlanDTO>>() {};
        try {
            ResponseEntity<List<BillingPlanDTO>> resp = restTemplate.exchange(configuration.url() + EndpointConfig.PAYMENT_SERVICE_PROVIDER_BASE_URL + "/api/billing-plans",
                    HttpMethod.POST, request, responseType);
            billingPlanDTOS = resp.getBody();

        } catch (RestClientException e) {
            e.printStackTrace();
        }
        System.out.println("Vratio se sa psp-a");
        System.out.println(billingPlanDTOS);
        return billingPlanDTOS;
    }

    @Override
    public Boolean readerHasSubscription(Reader reader, Merchant merchant) {
        List<Subscription> subscriptions = subscriptionRepository.readerHasSubscription(reader.getId(), merchant.getId(), LocalDate.now());
        if(subscriptions.size() == 0){
            return false;
        }
        else {
            for(Subscription subscription: subscriptions)
            {
                if(subscription.getStatus().equals(SubscriptionStatus.INITIATED) || subscription.getStatus().equals(SubscriptionStatus.CREATED) || subscription.getStatus().equals(SubscriptionStatus.COMPLETED))
                {
                    return true;
                }
            }
        }
        return false; // moze da ima expired, failed, canceled i da napravi novi subscription
    }

    @Override
    public ResponseEntity<?> sendSubscriptionToPC(SubscriptionRequestDTO dto, Reader reader, Merchant merchant) {
        System.out.println("Merchant: ");
        System.out.println(merchant.getMerchantEmail());
        dto.setMerchantEmail(merchant.getMerchantEmail());

        HttpEntity<SubscriptionRequestDTO> request = new HttpEntity<>(dto);
        ResponseEntity<SubscriptionResponseDTO> response = null;

        try {
            response = restTemplate.exchange(configuration.url() + EndpointConfig.PAYMENT_SERVICE_PROVIDER_BASE_URL + "/api/subscriptions/subscribe",
                    HttpMethod.POST, request, SubscriptionResponseDTO.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public List<SubscriptionHistoryDTO> getSubscriptions() {

        Authentication currentReader = SecurityContextHolder.getContext().getAuthentication();
        String username = currentReader.getName();
        List<Subscription> subscriptions = this.subscriptionRepository.getSubscriptions(username);
        List<SubscriptionHistoryDTO> subscriptionsDTO = new ArrayList<>();
        for (Subscription s : subscriptions) {
            SubscriptionHistoryDTO subscriptionDTO = new SubscriptionHistoryDTO(s);
            subscriptionsDTO.add(subscriptionDTO);
        }
        return subscriptionsDTO;
    }

    @Scheduled(initialDelay = 60000, fixedRate = 300000) //delay je 1 min, posle na svakih 5 minuta
    public void updateSubscriptionStatus(){

        System.out.println("Updating subscription status started...");
        //pronadju se INITIATED i CREATED
        List<Subscription> unfinishedSubscriptions = this.subscriptionRepository.findAllUnfinishedSubscriptions();

        for(Subscription s : unfinishedSubscriptions){

            ResponseEntity<SubscriptionStatusDTO> response = null;
            try{
                //mora i email jer pc mogu koristiti razlicite aplikacije a one mogu imati ordere sa istim id
                String merchantEmail = s.getMerchant().getMerchantEmail();
                response = restTemplate.getForEntity("https://localhost:8083/psp-service/api/subscriptions/status?subscriptionId=" + s.getSubscriptionId() + "&merchantEmail=" + merchantEmail, SubscriptionStatusDTO.class);

            }catch(Exception e){
                e.printStackTrace();
                return;
            }

            if(response.getBody().getStatus() != null) {
                SubscriptionStatus status = SubscriptionStatus.valueOf(response.getBody().getStatus());
                if (!status.equals(s.getStatus())) {
                    System.out.println("Promenjen status sa " + s.getStatus().toString() + " na " + status.toString());
                    s.setStatus(status);
                    this.subscriptionRepository.save(s);
                }
            }
        }
        System.out.println("Updating subscription status finished...");

    }


}
