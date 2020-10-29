package dataObj;

/**
 * Interface para representar objetos que serão exportadas para um CSV, requerindo a formatação de suas informações
 */
public interface FormattableToCSV {
    /**
     * Formata as informações do objeto para ser escrito em um CSV
     * @return String formatada
     */
    public String formatToCSV();
}
