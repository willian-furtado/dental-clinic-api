package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.AppointmentDTO;
import com.sboot.api.dental_clinic_api.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public List<AppointmentDTO> getAll(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return appointmentService.getAll(startDate, endDate);
    }

    @GetMapping("/{id}")
    public AppointmentDTO getById(@PathVariable String id) {
        return appointmentService.getById(id);
    }

    @PostMapping
    public AppointmentDTO create(@RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.save(appointmentDTO);
    }

    @PutMapping("/{id}")
    public AppointmentDTO update(@PathVariable String id, @RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.update(id, appointmentDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        appointmentService.delete(id);
    }

    @PutMapping("/{id}/confirm")
    public AppointmentDTO confirm(@PathVariable String id) {
        return appointmentService.confirmAppointment(id);
    }
}
