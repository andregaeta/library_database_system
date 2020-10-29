package event;

/**
 * Interface para representar um objeto que esteja observando o EventManager
 */

public interface EventListener {
    /**
     * Realiza uma ação quando o evento ocorre
     * @param type tipo de evento
     * @param data informações do evento
     * @param <T> tipo das informações do evento fornecidas
     */
    public <T> void onTrigger(EventType type, T data);
}
