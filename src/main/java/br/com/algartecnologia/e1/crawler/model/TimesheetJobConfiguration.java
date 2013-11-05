package br.com.algartecnologia.e1.crawler.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author <a href="mailto:pviniciusfm@gmail.com">Paulo Vinicius F. Machado</a>
 */
public class TimesheetJobConfiguration {

    public Map<FilaDemanda, List<String>> tarefas;

    public TimesheetJobConfiguration() {
        this.tarefas = new EnumMap<FilaDemanda,List<String>>(FilaDemanda.class);
        createDefault();
    }

    private void preparaTarefas() {
        for (FilaDemanda demanda : FilaDemanda.values()) {
            tarefas.put(demanda, new ArrayList<String>());
        }
    }

    private void createDefault() {
        preparaTarefas();
        
        List<String> filaRequisitos = tarefas.get(FilaDemanda.REQUISITOS);
        filaRequisitos.add("Requisitos / OS %s");
        filaRequisitos.add("Correção de bugs de Requisitos / OS %s");

        List<String> filaDesenvolvimento = tarefas.get(FilaDemanda.DESENVOLVIMENTO);
        filaDesenvolvimento.add("Apoio ao desenvolvimento / OS %s");
        filaDesenvolvimento.add("Apoio ao teste / OS %s");
        filaDesenvolvimento.add("Reunião para alinhamento técnico com o cliente / OS %s");
        filaDesenvolvimento.add("Especificação técnica / OS %s");
        filaDesenvolvimento.add("Laudo de realização especial / OS %s");
        filaDesenvolvimento.add("Necessidade de alteração de escopo / OS %s");
        filaDesenvolvimento.add("Implementação da demanda / OS %s");
        filaDesenvolvimento.add("Liberação de demanda Desenvolvimento / OS %s");
        filaDesenvolvimento.add("Correção de bugs de software / OS %s");
        filaDesenvolvimento.add("Preparação e configuração de ambiente de desenvolvimento / OS %s");

        List<String> filaHomolog = tarefas.get(FilaDemanda.HOMOLOGACAO);
        filaHomolog.add("Homologação de demanda / OS %s");
        filaHomolog.add("Inclusão/Alteração de casos de teste / OS %s");
        filaHomolog.add("Abertura/Fechamento de bugs / OS %s");
        filaHomolog.add("Liberação de demanda Homologação / OS %s");
        filaHomolog.add("Apoio à equipe / OS %s");
        filaHomolog.add("Preparação de ambiente de testes / OS %s");
        filaHomolog.add("Análise e entendimento de OS %s");
    }

}
