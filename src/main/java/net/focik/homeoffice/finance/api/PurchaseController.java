package net.focik.homeoffice.finance.api;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.focik.homeoffice.finance.api.dto.BasicDto;
import net.focik.homeoffice.finance.api.dto.CardDto;
import net.focik.homeoffice.finance.api.dto.PurchaseDto;
import net.focik.homeoffice.finance.api.mapper.ApiPurchaseMapper;
import net.focik.homeoffice.finance.domain.card.Card;
import net.focik.homeoffice.finance.domain.purchase.Purchase;
import net.focik.homeoffice.finance.domain.purchase.port.primary.AddPurchaseUseCase;
import net.focik.homeoffice.finance.domain.purchase.port.primary.DeletePurchaseUseCase;
import net.focik.homeoffice.finance.domain.purchase.port.primary.GetPurchaseUseCase;
import net.focik.homeoffice.finance.domain.purchase.port.primary.UpdatePurchaseUseCase;
import net.focik.homeoffice.utils.UserHelper;
import net.focik.homeoffice.utils.exceptions.ExceptionHandling;
import net.focik.homeoffice.utils.exceptions.HttpResponse;
import net.focik.homeoffice.utils.share.ActiveStatus;
import net.focik.homeoffice.utils.share.PaymentStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/finance/purchase")
//@CrossOrigin
public class PurchaseController extends ExceptionHandling {

    private final ApiPurchaseMapper mapper;
    private final AddPurchaseUseCase addPurchaseUseCase;
    private final UpdatePurchaseUseCase updatePurchaseUseCase;
    private final GetPurchaseUseCase getPurchaseUseCase;
    private final DeletePurchaseUseCase deletePurchaseUseCase;


    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('GOAHEAD_READ_ALL')")
    ResponseEntity<PurchaseDto> getById(@PathVariable int id) {

        log.info("Try find purchase by id: " + id);

        Purchase purchase = getPurchaseUseCase.findById(id);

        log.info(purchase != null ? "Found purchase for id = " + id : "Not found purchase for id = " + id);

        if (purchase == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(mapper.toDto(purchase), OK);
    }

    @GetMapping
//    @PreAuthorize("hasAnyAuthority('GOAHEAD_READ_ALL')")
    ResponseEntity<Map<String, List<PurchaseDto>>> getAll(@RequestParam PaymentStatus status,
                                             @RequestParam(required = false) LocalDate date) {

        log.info(String.format("Try get all purchase for user: '%s', status = '%s' and date = '%s",UserHelper.getUserName(), status, date));

        Map<LocalDate, List<Purchase>> purchaseMap = getPurchaseUseCase.findByUserMap(UserHelper.getUserName(), status, date);

        log.info("Found " + purchaseMap.size() + " purchases.");

        return new ResponseEntity<>(mapper.toDto(purchaseMap), OK);
    }

    @GetMapping("/user/{userId}")
        //    @PreAuthorize("hasAnyAuthority('GOAHEAD_READ_ALL')")
    ResponseEntity<Map<String, List<PurchaseDto>>>  getByUser(@PathVariable int userId, @RequestParam(required = false) PaymentStatus status,
                                            @RequestParam(required = false) LocalDate date) {
        log.info(String.format("Try get all purchase for userID: '%s', status = '%s' and date = '%s", userId, status, date));

        Map<LocalDate, List<Purchase>> purchaseMap = getPurchaseUseCase.findByUserMap(userId, status, date);

        log.info("Found " + purchaseMap.size() + " purchases.");

        return new ResponseEntity<>(mapper.toDto(purchaseMap), OK);
    }

    @GetMapping("/current")
        //    @PreAuthorize("hasAnyAuthority('GOAHEAD_READ_ALL')")
    ResponseEntity<Map<String, List<PurchaseDto>>>  getCurrent() {
        log.info(String.format("Try get current purchases"));

        Map<LocalDate, List<Purchase>> purchaseMap = getPurchaseUseCase.findCurrent();

        log.info("Found " + purchaseMap.size() + " purchases.");

        return new ResponseEntity<>(mapper.toDto(purchaseMap), OK);
    }

    @PostMapping
//    @PreAuthorize("hasAnyAuthority('GOAHEAD_WRITE_ALL')")
    public ResponseEntity<PurchaseDto> addCard(@RequestBody PurchaseDto purchaseDto) {
        log.info("Try add new purchase.");

        Purchase card = mapper.toDomain(purchaseDto);
        Purchase result = addPurchaseUseCase.addPurchase(card);

        log.info(result.getId() > 0 ? "purchase added with id = " + result : "No purchase added!");

        if (result.getId() <= 0)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(mapper.toDto(result), HttpStatus.CREATED);
    }

    @PutMapping
//    @PreAuthorize("hasAnyAuthority('GOAHEAD_WRITE_ALL')")
    public ResponseEntity<PurchaseDto> updateBank(@RequestBody PurchaseDto purchaseDto) {
        log.info("Try update purchase with id: {}", purchaseDto.getId());

        Purchase card = updatePurchaseUseCase.updatePurchase(mapper.toDomain(purchaseDto));
        return new ResponseEntity<>(mapper.toDto(card), OK);
    }

    @PutMapping("/status/{id}")
    //    @PreAuthorize("hasAnyAuthority('GOAHEAD_READ_ALL')")
    public ResponseEntity<PurchaseDto> updatePurchaseStatus(@PathVariable int id, @RequestBody BasicDto basicDto) {
        log.info("Try update perchase status.");

        Purchase purchase = updatePurchaseUseCase.updatePurchaseStatus(id, PaymentStatus.valueOf(basicDto.getValue()));
        return new ResponseEntity<>(mapper.toDto(purchase), OK);
    }

    @DeleteMapping("/{idPurchase}")
//    @PreAuthorize("hasAnyAuthority('GOAHEAD_DELETE_ALL')")
    public ResponseEntity<HttpResponse> deleteCard(@PathVariable int idPurchase) {
        log.info("Try purchase card with id: " + idPurchase);

        deletePurchaseUseCase.deletePurchase(idPurchase);

        log.info("Deleted purchase with id = " + idPurchase);

        return response(HttpStatus.NO_CONTENT, "Zakup usunięty.");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus status, String message) {
        HttpResponse body = new HttpResponse(status.value(), status, status.getReasonPhrase(), message);
        return new ResponseEntity<>(body, status);
    }
}
