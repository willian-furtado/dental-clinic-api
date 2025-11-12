package com.sboot.api.dental_clinic_api.mapper;

import com.sboot.api.dental_clinic_api.dto.AppointmentDTO;
import com.sboot.api.dental_clinic_api.entity.Appointment;
import com.sboot.api.dental_clinic_api.enums.AppointmentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {AppointmentStatus.class, java.time.LocalDate.class, java.time.LocalTime.class, java.time.LocalDateTime.class})
public  interface AppointmentMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.name")
    @Mapping(target = "status", expression = "java(appointment.getStatus().name().toLowerCase())")
    @Mapping(target = "date", expression = "java(appointment.getDate().toString())")
    @Mapping(target = "time", expression = "java(appointment.getTime().toString())")
    @Mapping(target = "createdAt", expression = "java(appointment.getCreatedAt() != null ? appointment.getCreatedAt().toString() : null)")
    AppointmentDTO toDto(Appointment appointment);

    @Mapping(target = "status", expression = "java(AppointmentStatus.valueOf(appointmentDTO.getStatus().toUpperCase()))")
    @Mapping(target = "date", expression = "java(LocalDate.parse(appointmentDTO.getDate()))")
    @Mapping(target = "time", expression = "java(LocalTime.parse(appointmentDTO.getTime()))")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    Appointment toEntity(AppointmentDTO appointmentDTO);
}
