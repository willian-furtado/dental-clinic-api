package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.AnamnesisResponseDTO;
import com.sboot.api.dental_clinic_api.dto.PatientDTO;
import com.sboot.api.dental_clinic_api.dto.PatientDocumentDTO;
import com.sboot.api.dental_clinic_api.entity.*;
import com.sboot.api.dental_clinic_api.mapper.AddressMapper;
import com.sboot.api.dental_clinic_api.mapper.AnamnesisResponseMapper;
import com.sboot.api.dental_clinic_api.mapper.DocumentMapper;
import com.sboot.api.dental_clinic_api.mapper.PatientMapper;
import com.sboot.api.dental_clinic_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientDocumentRepository patientDocumentRepository;
    private final AnamnesisQuestionOptionRepository optionRepository;
    private final AnamnesisQuestionRepository questionRepository;
    private final AnamnesisTemplateRepository anamnesisTemplateRepository;
    private final PatientAnamnesisRepository patientAnamnesisRepository;

    private final PatientMapper patientMapper;
    private final AddressMapper addressMapper;
    private final DocumentMapper documentMapper;
    private final AnamnesisResponseMapper anamnesisResponseMapper;

    @Transactional
    public PatientDTO save(PatientDTO patientDTO) {
        log.info("Saving new patient with name: {}", patientDTO.getName());
        Patient patient = patientMapper.toEntity(patientDTO);

        handleRelations(patient);
        handlePhoto(patient, patientDTO);
        handleAnamnesisResponses(patient, patientDTO);

        Patient saved = patientRepository.save(patient);
        log.info("Patient saved successfully with ID: {}", saved.getId());

        handlePatientTemplate(saved, patientDTO);

        return patientMapper.toDto(saved);
    }

    public PatientDTO update(String id, PatientDTO patientDTO) {
        log.info("Updating patient with ID: {}", id);
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id " + id));

        updateBasicInfo(existing, patientDTO);
        handlePhoto(existing, patientDTO);

        PatientAddress address = addressMapper.toEntity(patientDTO.getAddress());
        address.setPatient(existing);
        existing.setAddress(address);

        existing.getDocuments().clear();
        List<PatientDocument> documents = patientDTO.getDocuments().stream()
                .map(documentMapper::toEntity)
                .peek(d -> d.setPatient(existing))
                .toList();
        existing.getDocuments().addAll(documents);

        List<AnamnesisResponse> responses = patientDTO.getAnamnesisResponses().stream()
                .map(anamnesisResponseMapper::toEntity)
                .peek(r -> r.setPatient(existing))
                .toList();

        existing.getAnamnesisResponses().clear();
        existing.getAnamnesisResponses().addAll(responses);

        Patient saved = patientRepository.save(existing);
        log.info("Patient updated successfully with ID: {}", saved.getId());
        return patientMapper.toDto(saved);
    }


    public void delete(String id) {
        log.info("Deleting patient with ID: {}", id);
        if (!patientRepository.existsById(id)) {
            log.warn("Patient not found with ID: {}", id);
            throw new RuntimeException("Patient not found with id " + id);
        }
        patientRepository.deleteById(id);
        log.info("Patient deleted successfully with ID: {}", id);
    }


    @Transactional
    public Page<PatientDTO> getAll(Pageable pageable, String search) {
        if (search == null || search.trim().isEmpty()) {
            log.debug("Retrieving all patients with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
            Page<PatientDTO> result = patientRepository.findAll(pageable)
                    .map(patientMapper::toDto);
            log.debug("Retrieved {} patients", result.getTotalElements());
            return result;
        } else {
            log.debug("Searching patients by name or CPF: '{}' with pagination: page={}, size={}", search.trim(), pageable.getPageNumber(), pageable.getPageSize());
            Page<PatientDTO> result = patientRepository.findByNameOrCpf(pageable, search.trim())
                    .map(patientMapper::toDto);
            return result;
        }
    }

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toDto)
                .toList();
    }

    public PatientDTO getById(String id) {
        log.debug("Retrieving patient by ID: {}", id);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id " + id));
        return patientMapper.toDto(patient);
    }

    public void removePhoto(String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id " + id));
        patient.setPhoto(null);
        patientRepository.save(patient);
    }

    public void deleteDocument(String patientId, String documentId) {
        PatientDocument document = patientDocumentRepository.findByIdAndPatientId(documentId, patientId)
                .orElseThrow(() -> new RuntimeException("Document not found for this patient"));
        patientDocumentRepository.delete(document);
    }

    public PatientDocumentDTO saveDocument(String patientId, PatientDocumentDTO documentDTO) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id " + patientId));

        PatientDocument document = documentMapper.toEntity(documentDTO);
        document.setPatient(patient);
        document.setUploadDate(java.time.LocalDateTime.now());

        PatientDocument saved = patientDocumentRepository.save(document);
        return documentMapper.toDto(saved);
    }

    public List<PatientDTO> getTodayBirthdays() {
        log.debug("Retrieving patients with today's birthday");
        List<Patient> patients = patientRepository.findPatientsWithTodayBirthday();
        List<PatientDTO> result = patients.stream()
                .map(patientMapper::toDto)
                .toList();
        log.debug("Found {} patients with today's birthday", result.size());
        return result;
    }

    private void handleRelations(Patient patient) {
        if (patient.getDocuments() != null) {
            patient.getDocuments().forEach(doc -> doc.setPatient(patient));
        }
        if (patient.getAddress() != null) {
            patient.getAddress().setPatient(patient);
        }
    }

    private void handlePhoto(Patient patient, PatientDTO patientDTO) {
        String photo = patientDTO.getPhoto();

        if (photo != null && !photo.isEmpty()) {
            String base64;

            if (photo.contains(",")) {
                base64 = photo.split(",")[1];
            } else {
                base64 = photo;
            }
            byte[] photoBytes = Base64.getDecoder().decode(base64);
            patient.setPhoto(photoBytes);
        }
    }


    private void handleAnamnesisResponses(Patient patient, PatientDTO patientDTO) {
        if (patientDTO.getAnamnesisResponses() == null) return;

        List<AnamnesisResponse> responses = new ArrayList<>();

        for (AnamnesisResponseDTO responseDTO : patientDTO.getAnamnesisResponses()) {
            AnamnesisResponse response = new AnamnesisResponse();
            response.setPatient(patient);

            if (responseDTO.getQuestionId() != null) {
                AnamnesisQuestion question = questionRepository.findById(responseDTO.getQuestionId())
                        .orElseThrow(() -> new RuntimeException("Question not found"));
                response.setQuestion(question);
            }

            if (responseDTO.getSelectedOptionId() != null) {
                AnamnesisQuestionOption option = optionRepository.findById(responseDTO.getSelectedOptionId())
                        .orElseThrow(() -> new RuntimeException("Option not found"));
                response.setSelectedOption(option);
            }

            response.setValue(responseDTO.getValue());
            response.setExtraText(responseDTO.getExtraText());
            response.setCreatedAt(LocalDateTime.now());

            responses.add(response);
        }

        patient.setAnamnesisResponses(responses);
    }

    private void handlePatientTemplate(Patient saved, PatientDTO patientDTO) {
        if (patientDTO.getSelectedTemplate() == null) return;

        AnamnesisTemplate template = anamnesisTemplateRepository.findById(patientDTO.getSelectedTemplate())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        PatientAnamnesis patientAnamnesis = PatientAnamnesis.builder()
                .patient(saved)
                .template(template)
                .createdAt(LocalDateTime.now())
                .build();

        patientAnamnesisRepository.save(patientAnamnesis);
    }

    private void updateBasicInfo(Patient existing, PatientDTO patientDTO) {
        existing.setName(patientDTO.getName());
        existing.setCpf(patientDTO.getCpf());
        existing.setPhone(patientDTO.getPhone());
        existing.setEmail(patientDTO.getEmail());
        existing.setBirthDate(patientDTO.getBirthDate());

        if (patientDTO.getGuardian() != null) {
            existing.setGuardianName(patientDTO.getGuardian().getName());
            existing.setGuardianCpf(patientDTO.getGuardian().getCpf());
            existing.setGuardianPhone(patientDTO.getGuardian().getPhone());
            existing.setGuardianEmail(patientDTO.getGuardian().getEmail());
            existing.setGuardianRelationship(patientDTO.getGuardian().getRelationship());
        } else {
            existing.setGuardianName(null);
            existing.setGuardianCpf(null);
            existing.setGuardianPhone(null);
            existing.setGuardianEmail(null);
            existing.setGuardianRelationship(null);
        }
    }
}
