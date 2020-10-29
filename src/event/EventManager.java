package event;

/**
 * Classe para gerir o fluxo de eventos do programa
 */
public class EventManager {

    //Implementação de singleton
    private static EventManager instance;
    static {
        instance = new EventManager();
    }

    public static EventManager getInstance(){
        if(instance == null)
            instance = new EventManager();
        return instance;
    }

    private Event newBorrowEvent = new Event(EventType.NEW_BORROW);
    private Event returnBorrowEvent = new Event(EventType.RETURN_BORROW);
    private Event newStudentEvent = new Event(EventType.NEW_STUDENT);
    private Event newBookEvent = new Event(EventType.NEW_BOOK);
    private Event newAuthorEvent = new Event(EventType.NEW_AUTHOR);
    private Event newQueueEvent = new Event(EventType.NEW_QUEUE);
    private Event updateQueueEvent = new Event(EventType.UPDATE_QUEUE);

    public Event getNewBorrowEvent() {
        return newBorrowEvent;
    }

    public void setNewBorrowEvent(Event newBorrowEvent) {
        this.newBorrowEvent = newBorrowEvent;
    }

    public Event getReturnBorrowEvent() {
        return returnBorrowEvent;
    }

    public void setReturnBorrowEvent(Event returnBorrowEvent) {
        this.returnBorrowEvent = returnBorrowEvent;
    }

    public Event getNewStudentEvent() {
        return newStudentEvent;
    }

    public void setNewStudentEvent(Event newStudentEvent) {
        this.newStudentEvent = newStudentEvent;
    }

    public Event getNewBookEvent() {
        return newBookEvent;
    }

    public void setNewBookEvent(Event newBookEvent) {
        this.newBookEvent = newBookEvent;
    }

    public Event getNewAuthorEvent() {
        return newAuthorEvent;
    }

    public void setNewAuthorEvent(Event newAuthorEvent) {
        this.newAuthorEvent = newAuthorEvent;
    }

    public Event getNewQueueEvent() {
        return newQueueEvent;
    }

    public void setNewQueueEvent(Event newQueueEvent) {
        this.newQueueEvent = newQueueEvent;
    }

    public Event getUpdateQueueEvent() {
        return updateQueueEvent;
    }

    public void setUpdateQueueEvent(Event updateQueueEvent) {
        this.updateQueueEvent = updateQueueEvent;
    }
}
