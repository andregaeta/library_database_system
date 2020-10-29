package event;

import java.util.ArrayList;

/**
 * Classe para representar um evento no programa
 */
public class Event {
    private ArrayList<EventListener> listeners;

    private final EventType type;

    public Event(EventType type) {
        this.type = type;
        setListeners(new ArrayList<>());
    }

    /**
     * Cadastra um EventListener como observador do evento
     * @param listener objeto que observará o evento
     */
    public void subscribe(EventListener listener){
        getListeners().add(listener);
    }

    /**
     * Remove um EventListener da lista de cadastros do evento
     * @param listener objeto que parará de observar o evento
     */
    public void unsubscribe(EventListener listener) {
        getListeners().remove(listener);
    }

    /**
     * Informa os objetos cadastrados da realização do evento
     * @param data informações do evento
     * @param <T> tipo das informações do evento
     */
    public <T> void trigger(T data){
        for(EventListener listener : getListeners()){
            listener.onTrigger(getType(), data);
        }
    }


    //getters e setters
    public ArrayList<EventListener> getListeners() {
        return listeners;
    }

    public void setListeners(ArrayList<EventListener> listeners) {
        this.listeners = listeners;
    }

    public EventType getType() {
        return type;
    }
}
