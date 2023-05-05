package net.focik.homeoffice.finance.infrastructure.jpa;

import lombok.AllArgsConstructor;
import net.focik.homeoffice.finance.domain.fee.Fee;
import net.focik.homeoffice.finance.domain.fee.FeeInstallment;
import net.focik.homeoffice.finance.domain.fee.port.secondary.FeeRepository;
import net.focik.homeoffice.finance.infrastructure.dto.FeeDbDto;
import net.focik.homeoffice.finance.infrastructure.dto.FeeInstallmentDbDto;
import net.focik.homeoffice.finance.infrastructure.mapper.JpaFeeMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Primary
@AllArgsConstructor
class FeeRepositoryAdapter implements FeeRepository {

    FeeDtoRepository feeDtoRepository;
    FeeInstallmentDtoRepository feeInstallmentDtoRepository;
    JpaFeeMapper mapper;


    @Override
    public Fee saveFee(Fee fee) {
        FeeDbDto saved = feeDtoRepository.save(mapper.toDto(fee));
        return mapper.toDomain(saved);
    }

    @Override
    public FeeInstallment saveFeeInstallment(FeeInstallment feeInstallment) {
        FeeInstallmentDbDto saved = feeInstallmentDtoRepository.save(mapper.toDto(feeInstallment));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Fee> findFeeById(Integer id) {
        Optional<FeeDbDto> byId = feeDtoRepository.findById(id);
        if (byId.isEmpty())
            return Optional.empty();

        return Optional.of(mapper.toDomain(byId.get()));
    }

    @Override
    public Optional<FeeInstallment> findFeeInstallmentById(Integer id) {
        Optional<FeeInstallmentDbDto> byId = feeInstallmentDtoRepository.findById(id);

        if (byId.isEmpty())
            return Optional.empty();

        return Optional.of(mapper.toDomain(byId.get()));
    }

    @Override
    public List<Fee> findFeeByUserId(Integer idUser) {
        return feeDtoRepository.findAllByIdUser(idUser).stream()
                .map(loanDto -> mapper.toDomain(loanDto))
                .collect(Collectors.toList());
    }

    @Override
    public List<Fee> findAll() {
        return feeDtoRepository.findAll().stream()
                .map(loanDto -> mapper.toDomain(loanDto))
                .collect(Collectors.toList());
    }

    @Override
    public List<FeeInstallment> findFeeInstallmentByUserIdAndDate(Integer idUser, LocalDate date) {
        List<FeeDbDto> feeByIdEmployee = feeDtoRepository.findAllByIdUser(idUser);
        List<FeeInstallmentDbDto> feeInstallmentDbDtos = new ArrayList<>();
        String dateFormat = date.getYear() + String.format("-%02d", date.getMonthValue());
        for (FeeDbDto dto : feeByIdEmployee) {
            feeInstallmentDbDtos.addAll(feeInstallmentDtoRepository.findAllByIdFeeAndDate(dto.getId(), dateFormat));
        }

        return feeInstallmentDbDtos.stream()
                .map(l -> mapper.toDomain(l))
                .collect(Collectors.toList());
    }

    @Override
    public List<FeeInstallment> findFeeInstallmentByFeeId(Integer loanId) {
        return feeInstallmentDtoRepository.findAllByIdFee(loanId).stream()
                .map(loanInstallmentDto -> mapper.toDomain(loanInstallmentDto))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFeeById(int idFee) {
        feeDtoRepository.deleteById(idFee);
    }

    @Override
    public void deleteFeeInstallmentById(int idFeeInstallment) {
        feeInstallmentDtoRepository.deleteById(idFeeInstallment);
    }

    @Override
    public void deleteFeeInstallmentByIdFee(int idFee) {
        feeInstallmentDtoRepository.deleteByIdFee(idFee);
    }
}