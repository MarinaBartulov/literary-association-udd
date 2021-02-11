package team16.literaryassociation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.dto.MerchantActivationDTO;
import team16.literaryassociation.dto.MerchantDTO;
import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.services.interfaces.MerchantService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping
    public ResponseEntity registerNewMerchant(@RequestBody @Valid MerchantDTO merchantDTO){

        Merchant merchant = this.merchantService.findByEmail(merchantDTO.getEmail());
        if(merchant != null){
            return ResponseEntity.badRequest().body("Merchant with that email already exist.");
        }

        MerchantDTO m;
        try {
            m = this.merchantService.registerNewMerchant(merchantDTO);
        } catch(Exception e){
              e.printStackTrace();
              return ResponseEntity.badRequest().body("Error occurred while registering merchant on Payment Concentrator.");
        }
        return new ResponseEntity(m, HttpStatus.OK);

    }

    @PutMapping(value = "/activate")
    public ResponseEntity finishMerchantRegistration(@RequestBody @Valid MerchantActivationDTO merchantActivationDTO){

        Merchant merchant = this.merchantService.findByEmail(merchantActivationDTO.getMerchantEmail());
        if(merchant == null){
            return ResponseEntity.badRequest().body("Merchant with that email does not exist.");
        }
        this.merchantService.finishRegistration(merchantActivationDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/active")
    public ResponseEntity getAllActiveMerchants(){

        return ResponseEntity.ok(this.merchantService.findAllActiveMerchants());
    }


}
