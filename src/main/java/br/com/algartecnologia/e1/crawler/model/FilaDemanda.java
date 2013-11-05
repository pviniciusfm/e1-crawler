package br.com.algartecnologia.e1.crawler.model;

/**
 *
 * @author <a href="mailto:pviniciusfm@gmail.com">Paulo Vinicius F. Machado</a>
 */
public enum FilaDemanda {

    REQUISITOS("requisitos"), HOMOLOGACAO("homologacao"), DESENVOLVIMENTO("desenvolvimento");

    private final String jsonKey;

    private FilaDemanda(final String jsonKey) {
        this.jsonKey = jsonKey;
    }

    public String getJsonKey() {
        return jsonKey;
    }

}
