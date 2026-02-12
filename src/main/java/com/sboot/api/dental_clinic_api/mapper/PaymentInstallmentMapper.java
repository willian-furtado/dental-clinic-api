package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.PaymentInstallmentDTO;
import com.sboot.api.dental_clinic_api.entity.PaymentInstallment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentInstallmentMapper {

    PaymentInstallmentDTO toDTO(PaymentInstallment paymentInstallment);

    PaymentInstallment toEntity(PaymentInstallmentDTO paymentInstallmentDTO);
}
