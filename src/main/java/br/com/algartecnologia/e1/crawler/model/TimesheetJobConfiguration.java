package br.com.algartecnologia.e1.crawler.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * 
 * @author <a href="mailto:pviniciusfm@gmail.com">Paulo Vinicius F. Machado</a>
 */
public class TimesheetJobConfiguration {

    public Map<FilaDemanda, List<String>> tarefas;

    public TimesheetJobConfiguration() {
        initTarefas();
        createDefault();
    }

    private void initTarefas(){
        this.tarefas = new EnumMap<FilaDemanda, List<String>>(FilaDemanda.class);
    }

    public TimesheetJobConfiguration(JSONObject obj) {
        initTarefas();
        parseJSONObject(obj);
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

    private void parseJSONObject(JSONObject obj) {
        for (FilaDemanda demanda : FilaDemanda.values()) {
            if (obj.containsKey(demanda.getJsonKey())) {
                this.tarefas.put(demanda, transformJsonArrayIntoList((JSONArray) obj.get(demanda.getJsonKey())));
            } else {
                this.tarefas.put(demanda, Collections.EMPTY_LIST);
            }
        }        
    }

    private static List<String> transformJsonArrayIntoList(JSONArray array) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < array.size(); i++) {
            result.add((String)array.get(i));
        }
        return result;
    }

}
