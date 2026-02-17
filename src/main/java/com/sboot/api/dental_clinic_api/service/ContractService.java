package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.ContractGenerationRequestDTO;
import com.sboot.api.dental_clinic_api.dto.ContractResponseDTO;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.entity.PatientAddress;
import com.sboot.api.dental_clinic_api.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractService {

    private final PatientRepository patientRepository;

    private static final String DENTIST_NAME = "Pâmila Maiane Furtado Silva";
    private static final String DENTIST_CRO = "CRO MG CD 49285";
    private static final String DENTIST_SPECIALTY = "cirurgiã-dentista, endodontista";
    private static final String DENTIST_ADDRESS = "Av. Primeiro de Junho, 420, sl. 601, Centro, Divinópolis/MG, CEP 35.500-002";
    private static final String DENTIST_CITY = "Divinópolis";

    public ContractResponseDTO generateContract(ContractGenerationRequestDTO request) {
        log.info("Generating contract for patient ID: {}", request.getPatientId());

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id " + request.getPatientId()));

        PatientAddress address = patient.getAddress();

        String patientName = patient.getName() != null ? patient.getName() : "";
        String patientCpf = formatCpf(patient.getCpf());
        String patientCity = address != null && address.getCity() != null ? address.getCity() : "";
        String patientAddress = formatPatientAddress(address);
        
        String currentDate = formatCurrentDate();
        String totalValueText = request.getTotalValue() != null && request.getTotalValue().compareTo(BigDecimal.ZERO) > 0
            ? formatCurrency(request.getTotalValue())
            : "";

        String template = "CONTRATO DE PRESTAÇÃO DE SERVIÇOS ODONTOLÓGICOS\n\n" +
                "CONTRATANTE: " + patientName + ",\n" +
                "natural de " + patientCity + ", residente à " +
                patientAddress + "\n" +
                "CPF: " + patientCpf + ".\n\n" +
                "CONTRATADO: " + DENTIST_NAME + ", " +
                DENTIST_SPECIALTY + ", " + DENTIST_CRO +
                " com consultório na " + DENTIST_ADDRESS + ".\n" +
                "As partes acima nomeadas e qualificadas têm entre si, justo e contratado, o quanto segue:\n\n" +
                "1° Cláusula\n" +
                "O CONTRATADO prestará ao CONTRATANTE, sem caráter de exclusividade, horário e subordinação, " +
                "serviços de tratamento odontológico (serviços de especialidade / endo / perio / orto / etc), " +
                "obedecidos os termos e condições previstos neste instrumento.\n\n" +
                "2° Cláusula – DO OBJETO\n" +
                "O objeto CONTRATADO pelas partes está discriminado no Plano de Tratamento Odontológico n°______" +
                " anexo e com valores de cada procedimento odontológicos, " +
                "autorizando com a assinatura deste a execução do mesmo, em detrimento de outras opções " +
                "de tratamento apresentadas. O CONTRATANTE foi devidamente esclarecido sobre riscos, " +
                "vantagens, desvantagens e alternativas de tratamento optando de livre e espontânea " +
                "vontade por este plano de tratamento.\n\n" +
                "3° Cláusula – DAS CONDIÇÕES DE PAGAMENTO\n" +
                "Caberá ao CONTRATANTE o pagamento de honorários profissionais no valor de R$ " +
                totalValueText + "\n" +
                "e com as seguintes condições de pagamento: {{paymentConditions}}.\n" +
                "O atraso em qualquer uma das parcelas autoriza o profissional cobrar multa sobre valor devido, " +
                "além de juros (Cod. Civil 2003) de atraso e a imediata suspensão do tratamento até que o " +
                "valor devido seja integralmente quitado.\n\n" +
                "4° Cláusula – ALTERAÇÃO NO PLANO DE TRATAMENTO\n" +
                "O Plano de Tratamento Odontológico poderá sofrer alterações durante o atendimento, e estas " +
                "serão executadas somente com o prévio consentimento do CONTRATANTE.\n\n" +
                "5° Cláusula – MARCAÇÃO DE CONSULTAS\n" +
                "O CONTRATADO atenderá o CONTRATANTE no consultório odontológico, mediante horário " +
                "previamente agendado por telefone ou pessoalmente.\n\n" +
                "6° Cláusula – ATRASOS, FALTAS E ABANDONO DE TRATAMENTO\n" +
                "O CONTRATANTE está ciente de que o atraso máximo permitido é de 15 minutos a partir do " +
                "qual perderá direito a consulta, sendo este ato considerado como falta. O CONTRATANTE está " +
                "ciente de que faltas sem justificativas são de sua responsabilidade e autoriza a cobrança " +
                "de R$ {{missedAppointmentFee}} por cada falta não comunicada com no mínimo {{minimumHours}} horas de antecedência já que o horário " +
                "estará disponibilizado ao CONTRATANTE, e que o não comparecimento sistemático as " +
                "consultas odontológicas sem justificativa ou não comparecimento sem justificativa por mais " +
                "de 30(trinta) dias caracterizara o abandono de tratamento por parte do CONTRATANTE.\n\n" +
                DENTIST_CITY + ", " + currentDate + ".\n\n\n";
        
        log.info("Contract generated successfully for patient: {}", patient.getName());

        return ContractResponseDTO.builder()
                .template(template)
                .editableFields(ContractResponseDTO.EditableFields.builder()
                        .paymentConditions("")
                        .missedAppointmentFee("")
                        .minimumHours("")
                        .build())
                .build();
    }

    private String formatPatientAddress(PatientAddress address) {
        if (address == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        
        if (address.getStreet() != null && !address.getStreet().isEmpty()) {
            sb.append(address.getStreet());
        }
        
        if (address.getNumber() != null && !address.getNumber().isEmpty()) {
            sb.append(", ").append(address.getNumber());
        }
        
        if (address.getComplement() != null && !address.getComplement().isEmpty()) {
            sb.append(", ").append(address.getComplement());
        }
        
        if (address.getNeighborhood() != null && !address.getNeighborhood().isEmpty()) {
            sb.append(" - ").append(address.getNeighborhood());
        }
        
        if (address.getCity() != null && !address.getCity().isEmpty()) {
            sb.append(" - ").append(address.getCity());
        }
        
        if (address.getState() != null && !address.getState().isEmpty()) {
            sb.append("/").append(address.getState());
        }
        
        if (address.getZipCode() != null && !address.getZipCode().isEmpty()) {
            sb.append(" - CEP: ").append(formatZipCode(address.getZipCode()));
        }

        return sb.toString();
    }

    private String formatCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return "";
        }
        if (cpf.length() == 11) {
            return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + 
                   cpf.substring(6, 9) + "-" + cpf.substring(9);
        }
        return cpf;
    }

    private String formatZipCode(String zipCode) {
        if (zipCode == null || zipCode.isEmpty()) {
            return "";
        }
        if (zipCode.length() == 8) {
            return zipCode.substring(0, 5) + "-" + zipCode.substring(5);
        }
        return zipCode;
    }

    private String formatCurrentDate() {
        LocalDate now = LocalDate.now();
        String day = formatDayPortuguese(now.getDayOfMonth());
        String month = formatMonthPortuguese(now.getMonthValue());
        int year = now.getYear();
        return day + " de " + month + " de " + year;
    }

    private String formatDayPortuguese(int day) {
        return switch (day) {
            case 1 -> "1°";
            case 21 -> "21°";
            case 31 -> "31°";
            default -> String.valueOf(day);
        };
    }

    private String formatMonthPortuguese(int month) {
        return switch (month) {
            case 1 -> "janeiro";
            case 2 -> "fevereiro";
            case 3 -> "março";
            case 4 -> "abril";
            case 5 -> "maio";
            case 6 -> "junho";
            case 7 -> "julho";
            case 8 -> "agosto";
            case 9 -> "setembro";
            case 10 -> "outubro";
            case 11 -> "novembro";
            case 12 -> "dezembro";
            default -> "";
        };
    }

    private String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "";
        }
        return String.format("%.2f", value.setScale(2, java.math.RoundingMode.HALF_UP)).replace(".", ",");
    }
}
