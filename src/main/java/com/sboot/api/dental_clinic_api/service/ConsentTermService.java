package com.sboot.api.dental_clinic_api.service;

import com.sboot.api.dental_clinic_api.dto.ConsentTermGenerationRequestDTO;
import com.sboot.api.dental_clinic_api.dto.ConsentTermResponseDTO;
import com.sboot.api.dental_clinic_api.entity.Patient;
import com.sboot.api.dental_clinic_api.entity.TreatmentPlan;
import com.sboot.api.dental_clinic_api.repository.PatientRepository;
import com.sboot.api.dental_clinic_api.repository.TreatmentPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsentTermService {
    private final PatientRepository patientRepository;
    private final TreatmentPlanRepository treatmentPlanRepository;

    public ConsentTermResponseDTO generateConsentTerm(ConsentTermGenerationRequestDTO request) {
        log.info("Generating consent term for patient ID: {} and treatment plan ID: {}", request.getPatientId(), request.getTreatmentPlanId());
        
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> {
                    log.error("Patient not found with ID: {}", request.getPatientId());
                    return new RuntimeException("Patient not found with id " + request.getPatientId());
                });
        TreatmentPlan plan = treatmentPlanRepository.findById(request.getTreatmentPlanId())
                .orElseThrow(() -> {
                    log.error("TreatmentPlan not found with ID: {}", request.getTreatmentPlanId());
                    return new RuntimeException("TreatmentPlan not found with id " + request.getTreatmentPlanId());
                });

        boolean hasEndodontia = plan.getProcedures().stream()
                .anyMatch(proc -> proc.getProcedureClinic() != null &&
                        "Endodontia".equalsIgnoreCase(proc.getProcedureClinic().getCategory()));
        
        if (hasEndodontia) {
            log.info("Generating endodontia consent term for patient: {}", patient.getName());
            String termo = montarTermoConsentimentoEndodontia(patient);
            return ConsentTermResponseDTO.builder()
                    .template(termo)
                    .editableFields(null)
                    .build();
        } else {
            log.info("Generating general consent term for patient: {}", patient.getName());
            String termo = montarTermoConsentimentoGeral(patient);
            ConsentTermResponseDTO.EditableFields editableFields = ConsentTermResponseDTO.EditableFields.builder()
                    .currentHealthCondition("")
                    .requiredCare("")
                    .treatmentRisks("")
                    .build();
            return ConsentTermResponseDTO.builder()
                    .template(termo)
                    .editableFields(editableFields)
                    .build();
        }
    }

    private String montarTermoConsentimentoEndodontia(Patient patient) {
        log.debug("Building endodontia consent term template");
        String nome = patient.getName() != null ? patient.getName() : "";
        String cpf = patient.getCpf() != null ? patient.getCpf() : "";
        String rg = patient.getRg() != null ? patient.getRg() : "";
        String dentes = "___________";
        String cro = "CRO MG CD 49285";
        return "TERMO DE CONSENTIMENTO LIVRE ESCLARECIDO PARA ENDODONTIA\n" +
                "Pelo presente instrumento, eu " + nome + " CPF:" + cpf + " e RG: " + rg + " declaro que fui suficientemente esclarecido  (a) pela cirurgiã dentista PAMILA MAIANE FURTADO SILVA  com registro no conselho CRO n. " + cro + " que necessito de (re) tratamento endodôntico (tratamento de canal) do (s)  dente (s) " + dentes + "  com atendimento estabelecido à Rua Primeiro de Junho 420, sala 601, Edifício Paulo VI, bairro centro, CEP 35500002. Profissional escolhido para realizar o tratamento descrito no planejamento de custos, declaro que:\n" +
                "Estou ciente sobre o método/tratamento proposto, o qual foi me explicado, as vantagens do mesmo e os riscos os quais estarei sujeito, tais como:\n" +
                "1. Existe um índice de insucesso de 5% a 10% nos tratamentos endodônticos e de 15% a 40% em retratamento. Se houver falha no tratamento endodôntico, poderá ser necessário um retratamento, cirurgia ou até mesmo a extração do dente para casos de maior complexidade;\n" +
                "2. Se ocorrer fratura de instrumentos dentro da raiz do dente, o dentista irá decidir se deixará no local ou se solicitará uma intervenção cirúrgica para remoção da mesma. Cada caso é examinado cuidadosamente pelo profissional dentista, já que existem situações onde não há necessidade de remoção desses eventuais instrumentos;\n" +
                "3. Pode ocorrer perfuração do canal radicular com uso de instrumentos, casos que poderão culminar na necessidade de uma pequena cirurgia;\n" +
                "4. A presença de calcificações, variações do padrão de normalidade, curvaturas e lesões são agravantes da condição de tratamento, tornando-o mais complexo. E, em alguns casos, pode ser necessária a realização de uma cirurgia periapical;\n" +
                "5. Estou ciente que após o (re) tratamento endodôntico meu dente deverá ser protegido de fraturas e contaminações, sendo necessária a restauração do mesmo o mais imediato possível;\n" +
                "6. Em decorrência da fragilidade natural do dente que está em tratamento, pode ocorrer a sua fratura durante ou no intervalo entre as sessões devendo, portanto, o paciente, manter um cuidado maior durante a mastigação;\n" +
                "7. Todo dente que recebeu tratamento de canal precisa ser acompanhado por um tempo mínimo de 02 anos, principalmente se houver lesão no Periápice do dente em questão;\n" +
                "8. A remoção dos retentores, pinos intra-radiculares, assim como determinados tipos de restaurações metálicas fundidas, blocos e/ou incrustações pode gerar trincas e levar a fratura do dente em questão. Portanto, é um procedimento de risco;\n" +
                "9. O dente tratado de canal é um dente desidratado, sem vida, e torna-se mais seco e quebradiço. Por isso é mais propenso à fratura do que os outros que não tiveram tratamento endodôntico. Pode ser necessário a colocação de um pino para promover um reforço na estrutura restante, um bloco recobrindo as cúspides do dente e então a substituição do elemento por pino acrescido de uma coroa total;\n" +
                "10. O tratamento acaba com a restauração definitiva e/ou com a reabilitação protética do dente que recebeu tratamento anterior.\n" +
                "11. As consultas são combinadas entre o profissional e o paciente. O não retorno nas datas estabelecidas pode complicar o tratamento e favorecer a infecção;\n" +
                "12. Quando o nervo do dente é retirado, ocorre uma série de modificações químicas dentro do dente, e em alguns casos pode ocorrer o escurecimento da coroa com o passar do tempo;\n" +
                "13. É normal um desconforto pós-operatório, que pode durar alguns dias, meses, até a normalização da atividade inflamatória no local. Neste caso, será necessário o uso de alguns medicamentos prescritos pelo dentista;\n" +
                "14. A solicitação de exames complementares de alta resolução, como a tomografia computadorizada, colaboram para o diagnóstico, planejamento e preservação do elemento dental em questão;\n" +
                "15. Como profissional escolhido para realização do tratamento, zelo pela prestação dos serviços com os melhores cuidados de saúde oral ao meu alcance, agindo com correção e delicadeza, a fim de satisfazer as necessidades para o tratamento em tela.\n" +
                "Li os termos acima, que também me foram explicados. Tive oportunidade de questionar e sanar cada um dos tópicos deste Termo De Esclarecimento junto ao profissional para meu entendimento acerca do tratamento proposto. Dessa forma, estando minhas dúvidas esclarecidas, autorizo a execução do (re) tratamento.";
    }

    private String montarTermoConsentimentoGeral(Patient patient) {
        log.debug("Building general consent term template");
        String nome = patient.getName() != null ? patient.getName() : "";
        String cpf = patient.getCpf() != null ? patient.getCpf() : "";
        String rg = patient.getRg() != null ? patient.getRg() : "";
        String endereco = formatPatientAddress(patient.getAddress());
        String responsavel = patient.getGuardianName() != null ? patient.getGuardianName() : "";
        String responsavelTrecho = responsavel.isEmpty() ? "" : " [ou responsável portador(a) do legal do(a) menor " + responsavel + "]";
        return "TERMO DE CONSENTIMENTO LIVRE E ESCLARECIDO\n" +
                "Pelo presente termo de consentimento livre e esclarecido, eu, " + nome + responsavelTrecho + " portador(a) do RG nº:" + rg + ", CPF:" + cpf + ", residente:" + endereco + ", declaro que a cirurgiã  Pâmila Maiane Furtado Silva, cirurgiã-dentista, endodontista, CRO MG CD 49285 com consultório na Av. Primeiro de Junho, 420, sl. 601, Centro, Divinópolis/MG, CEP 35.500-002, profissional escolhida para realizar o tratamento descrito no planejamento de tratamento e planejamento de custos, constante em meu prontuário, cuja cópia encontra-se em meu poder e sob a minha guarda, declaro que:\n" +
                "1. A ficha de anamnese foi por mim preenchida e assinada, apresentando informações que correspondem à verdade dos fatos, especialmente no que diz respeito às minhas condições da saúde geral e bucal, não tendo omitido ou suprimido qualquer dado quanto a doenças pre-existentes e que sejam de meu conhecimento, tão pouco quanto ao uso de medicamentos controlados ou não, ciente de que a omissão de dados sobre a minha saúde geral e bucal e sobre o uso de medicamentos pode interferir negativamente no planejamento e andamento de tratamento, na resposta biológica do meu organismo à técnica empregada, podendo ocasionar danos irreversíveis à minha saúde bucal e geral, inclusive quando do uso de substâncias medicamentosas utilizadas durante o procedimento odontológico ou prescritas no transcorrer do tratamento, que podem dar causa à problemas cardíacos, alergias e até a morte;\n" +
                "2. Considerando minha queixa principal e, após avaliação clínica e de eventuais exames complementares, a profissional me esclareceu sobre o diagnóstico e planejamento de tratamento, com alternativas e informações claras sobre os objetivos e riscos do planejamento terapêutico escolhido, bem como sobre minha responsabilidade de colaborar e contribuir para o tratamento que será executado;\n" +
                "3. É de meu conhecimento de que o tratamento proposto será realizado aproximadamente em ___________________, podendo, todavia, sofrer prorrogação ou alteração de prazo, de acordo com eventual complexidade que o caso apresentar no decorrer do tratamento, bem como pela resposta biológica do meu organismo à técnica empregada, assiduidade às consultas e seguimento das orientações fornecidas pela profissional;\n" +
                "4. Declaro, ainda, que estou ciente que eventuais ausências às consultas e o não atendimento das orientações profissionais prejudicarão o resultado pretendido, uma vez que a Odontologia não se trata de uma ciência exata, sofrendo limitações;\n" +
                "5. Declaro que estou ciente de que deverei comparecer pontualmente no consultório da profissional, nas sessões, previamente agendadas, devendo seguir, rigorosamente, as prescrições, encaminhamentos a outros especialistas da área odontológica ou profissionais da área de saúde e demais orientações fornecidas pela profissional, sob pena de ser interrompido o tratamento;\n" +
                "6. É de meu conhecimento de que devo informar ao profissional qualquer alteração em decorrência do tratamento realizado, insatisfações ou dúvidas sobre o tratamento em execução; mantendo meus dados cadastrais sempre atualizados e informando eventuais mudanças de endereço, telefone, etc.;\n" +
                "7. A cirurgiã-dentista declarou que a técnica proposta e demais materiais que serão utilizados no meu tratamento possuem efetiva comprovação cientifica, respeitando o mais alto nível profissional, o estado atual da ciência e sua dignidade profissional, sendo uma das alternativas de tratamento indicadas para o meu caso;\n" +
                "8. Estou ciente de que a Odontologia não é uma ciência exata e que os resultados esperados, a partir do diagnóstico, poderão não se concretizar em face da resposta biológica do meu organismo e de minha colaboração, assim como da própria limitação da ciência, sendo certo que a profissional se compromete a utilizar as técnicas e os materiais adequado, à execução do plano de tratamento proposto e aprovado, assumindo responsabilidade pelos serviços prestados, resguardando a minha privacidade e o necessário sigilo profissional, além de zelar por minha saúde e dignidade;\n" +
                "9. Tenho conhecimento de que a cirurgiã-dentista possui o dever de elaborar e manter atualizado o meu prontuário, conservando-o em arquivo próprio, me garantido acesso ao mesmo, sempre que for expressamente solicitado, podendo conceder cópia do documento, mediante recibo de entrega. Caso seja solicitada a devolução da documentação radiográfica e outros exames, a profissional se compromete a me devolver os documentos originais, após sua duplicação para arquivo do consultório. Se a profissional tiver suportado o custo dos exames, tenho ciência de que deverei arcar com o custo da duplicação;\n" +
                "10. Declaro estar ciente do plano de tratamento odontológico, em anexo, também de possíveis alterações que por ventura venham a ocorrer e que somente serão realizadas após meu consentimento expresso;\n" +
                "11. Entendo a importância da saúde bucal e me comprometo seguir as orientações da equipe odontológica, assim como retomar as consultas de orientações programadas. Entendo, ainda, que cada ser humano possui particularidades e respostas biológicas diversas, sendo que o procedimento odontológico, ainda que realizado por profissional habilitado, ou seja, cirurgião-dentista, e, ainda que realizado de acordo com técnica reconhecida cientificamente e indicada ao meu caso, com material de qualidade, respeitando passo a passo do que determina a literatura ou a ciência odontológica, pode acontecer de que a resposta e o resultado esperado não sejam parcial ou totalmente alcançados, uma vez que a Odontologia não é uma ciência exata;\n" +
                "12. Fui esclarecido (a) que, caso o tratamento proposto, durante a sua execução ou ao final, não alcançar a perspectiva almejada, com cura da doença ou reabilitação necessária, o profissional apresentará esclarecimentos, a todo instante, sobre as limitações enfrentadas propondo alternativas, quando houver;\n" +
                "13. Fui esclarecido (a) pela profissional que minhas condições atuais de saúde bucal e/ou geral se  apresentam da seguinte forma: {{currentHealthCondition}};\n" +
                "14. Fui esclarecido (a) pela profissional que em razão das condições descritas no item anterior (13),  deverei observar os seguintes cuidados: {{requiredCare}};\n" +
                "15. Fui esclarecido (a) pela profissional que o tratamento escolhido apresenta os seguintes riscos: {{treatmentRisks}};\n" +
                "16. Declaro, ainda, que tenho conhecimento de que ao término do tratamento deverei retomar para consultas de acompanhamento de acordo com os critérios estabelecidos pelo profissional, visando resguardar e manter o tratamento realizado, sendo certo que não é possível garantir o tempo de durabilidade dos procedimentos odontológicos, pois referida avaliação deverá observar as condições de minha saúde e eventuais alterações bucais, hábitos em geral, adequada higienização oral, além de outros fatores internos ou externos que podem danificar o serviço prestado. A profissional não se eximirá de avaliar eventual dano ou prejuízo sofrido e alegado, reparando-o, quando o caso, dentro do limite de sua responsabilidade;\n" +
                "17. Abaixo manifesto se permito a utilização do meu prontuário para uso em publicações cientificas ou com finalidade acadêmica, permitindo a exibição de imagens e exames com finalidade didático-acadêmicas, conforme previsto no Código de Ética Odontológica: (    ) Sim (    ) Não.";
    }

    private String formatPatientAddress(com.sboot.api.dental_clinic_api.entity.PatientAddress address) {
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

    private String formatZipCode(String zipCode) {
        if (zipCode == null || zipCode.isEmpty()) {
            return "";
        }
        if (zipCode.length() == 8) {
            return zipCode.substring(0, 5) + "-" + zipCode.substring(5);
        }
        return zipCode;
    }
}