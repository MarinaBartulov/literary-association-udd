package team16.literaryassociation.controller;

import org.camunda.bpm.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.*;
import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.model.Subscription;
import team16.literaryassociation.services.interfaces.MerchantService;
import team16.literaryassociation.services.interfaces.SubscriptionService;
import team16.literaryassociation.services.interfaces.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;

    @Autowired
    private IdentityService identityService;

    Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @GetMapping(value = "/billing-plans/{merchantId}")
    public ResponseEntity<?> getAllMerchantBillingPlans(@PathVariable Long merchantId)
    {
        Merchant merchant = merchantService.findOne(merchantId);
        if(merchant == null)
        {
            logger.error("Merchant not found | ID: " + merchantId);
            return new ResponseEntity<>("Merchant not found",HttpStatus.NOT_FOUND);
        }

        try {
            List<BillingPlanDTO> billingPlansDTO = subscriptionService.getMerchantBillingPlans(merchant.getMerchantEmail());
            System.out.println("Duzina liste billing planova: "+ billingPlansDTO.size());
            return new ResponseEntity<>(billingPlansDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Couldn't retrieve billing plans",HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value="/subscribe", produces = "application/json")
    public ResponseEntity<?> subscribe(@Valid @RequestBody SubscriptionRequestDTO dto) throws Exception {

        String username = this.identityService.getCurrentAuthentication().getUserId();
        Reader reader = (Reader) userService.findByUsername(username);
        if(reader == null)
        {
            logger.error("Reader not found | Username: " + username);
            return new ResponseEntity<>("Reader not found", HttpStatus.NOT_FOUND);
        }

        Merchant merchant = merchantService.findOne(dto.getMerchantId());
        if(merchant == null) {
            logger.error("Merchant not found | Username: " + username);
            return new ResponseEntity<>("Merchant not found", HttpStatus.NOT_FOUND);
        }

        if( subscriptionService.readerHasSubscription(reader, merchant) )
        {
            logger.error("Reader already has subscription to this merchant");
            return new ResponseEntity<>("Reader already has subscription to this merchant", HttpStatus.EXPECTATION_FAILED);
        }

        try{
            ResponseEntity<?> response = subscriptionService.sendSubscriptionToPC(dto, reader, merchant);
            if(response.getStatusCode().equals(HttpStatus.OK))
            {
                SubscriptionResponseDTO subscriptionResponseDTO = (SubscriptionResponseDTO) response.getBody();
                Subscription subscription = new Subscription(subscriptionResponseDTO, merchant, reader);
                subscriptionService.save(subscription); // kada je napravljena na PSP onda se cuva na LA sa statusom PENDING
                return new ResponseEntity<>(subscriptionResponseDTO.getRedirectURL(), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(response.getBody(), HttpStatus.BAD_REQUEST);

        }catch (Exception e) {
            return new ResponseEntity<>("Publisher doesn't have payment method for paying subscription",HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public ResponseEntity getSubscriptions(){

        List<SubscriptionHistoryDTO> subscriptionsDTO = this.subscriptionService.getSubscriptions();
        return new ResponseEntity(subscriptionsDTO, HttpStatus.OK);
    }

}
