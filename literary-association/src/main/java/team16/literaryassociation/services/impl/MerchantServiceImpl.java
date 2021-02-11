package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team16.literaryassociation.dto.*;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.model.Genre;
import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.repository.MerchantRepository;
import team16.literaryassociation.services.interfaces.MerchantService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private BookServiceImpl bookService;
    @Autowired
    private GenreServiceImpl genreService;
    @Autowired
    private WriterServiceImpl writerService;

    @Value("${application_id}")
    private String appId;
    @Value("${success_url}")
    private String successUrl;
    @Value("${failed_url}")
    private String failedUrl;
    @Value("${error_url}")
    private String errorUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Merchant findOne(Long id) {
        return merchantRepository.getOne(id);
    }

    @Override
    public Merchant findByEmail(String email) {
        return this.merchantRepository.findByMerchantEmail(email);
    }

    @Override
    public MerchantDTO registerNewMerchant(MerchantDTO merchantDTO) {

        Merchant newMerchant = new Merchant();
        newMerchant.setMerchantName(merchantDTO.getName());
        newMerchant.setMerchantEmail(merchantDTO.getEmail());
        newMerchant.setActivated(false);
        newMerchant.setMerchantSuccessUrl(this.successUrl);
        newMerchant.setMerchantFailedUrl(this.failedUrl);
        newMerchant.setMerchantErrorUrl(this.errorUrl);

        MerchantPCDTO pcDTO = new MerchantPCDTO();
        pcDTO.setMerchantName(merchantDTO.getName());
        pcDTO.setMerchantEmail(merchantDTO.getEmail());
        pcDTO.setActivationUrl("https://localhost:8080/api/merchant/activate");
        pcDTO.setAppId(this.appId);
        pcDTO.setSuccessUrl(this.successUrl);
        pcDTO.setFailedUrl(this.failedUrl);
        pcDTO.setErrorUrl(this.errorUrl);
        ResponseEntity<MerchantPCDTO> response
                = restTemplate.postForEntity("https://localhost:8083/psp-service/api/merchant",
                pcDTO, MerchantPCDTO.class);

        this.merchantRepository.save(newMerchant);

        //dodam odmah novu knjigu zbog testiranja placanja
        Book newBook = new Book();
        newBook.setTitle("Knjiga od novog merchanta");
        newBook.setISBN("3241234324234");
        newBook.setNumOfPages(200);
        newBook.setOpenAccess(false);
        newBook.setPrice(10.45);
        Genre genre = this.genreService.findByName("Thriller");
        newBook.setGenre(genre);
        newBook.setPublisher(newMerchant);
        newBook.setPdf("/uploaded-files/knjiga3.pdf");
        newBook.setPublishersAddress("Adresa 2");
        Writer writer = this.writerService.findByUsername("writer123");
        newBook.setWriter(writer);
        newBook.setSynopsis("Ovo je sinopsis nove knjige");
        newBook.setYear("2020");
        this.bookService.save(newBook);

        return new MerchantDTO(newMerchant);
    }

    @Override
    public void finishRegistration(MerchantActivationDTO mbi) {

        Merchant m = this.merchantRepository.findByMerchantEmail(mbi.getMerchantEmail());
        m.setActivated(true);
//        if(mbi.isBankPaymentMethod()) {
//            m.setMerchantId(mbi.getMerchantId());
//            m.setMerchantPassword(mbi.getMerchantPassword());
//        }
        this.merchantRepository.save(m);
    }

    @Override
    public List<MerchantDTO> findAllActiveMerchants() {
        List<Merchant> merchants = this.merchantRepository.findAllActiveMerchants();
        List<MerchantDTO> ms = merchants.stream().map(m -> new MerchantDTO(m.getId(), m.getMerchantName(), m.getMerchantEmail())).collect(Collectors.toList());
        return ms;
    }


}
