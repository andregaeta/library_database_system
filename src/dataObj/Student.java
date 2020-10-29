package dataObj;

import event.EventListener;
import event.EventManager;
import event.EventType;
import util.Tools;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Student implements EventListener, FormattableToCSV{
    private int studentId;
    private LocalDate birthdate;
    private Gender gender;
    private String className;
    private int points;
    private ArrayList<BorrowData> currentBorrows;

    private String name;
    private String surname;


    private static HashMap<Integer, Student> studentById = new HashMap<>();

    public Student(int studentId, String name, String surname, LocalDate birthdate, Gender gender, String className, int points) {
        this.setStudentId(studentId);
        this.setName(name);
        this.setSurname(surname);
        this.setBirthdate(birthdate);
        this.setGender(gender);
        this.setClassName(className);
        this.setPoints(points);
        setCurrentBorrows(new ArrayList<>());
        EventManager.getInstance().getNewBorrowEvent().subscribe(this);
        EventManager.getInstance().getReturnBorrowEvent().subscribe(this);
    }

    /**
     * Carrega o aluno em memória a partir de uma string lida externamente
     * @param str String representando o aluno
     * @return objeto do aluno interpretado da String fornecida
     */

    public static Student createFromString(String str){

        String[] lines = str.split("\",");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replace("\"", "");
            lines[i] = lines[i].trim();
        }

        int id = Integer.parseInt(lines[0]);
        String name = lines[1];
        String surname = lines[2];
        LocalDate date = LocalDate.parse(lines[3], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Gender gender = (lines[4].toLowerCase().equals("m")) ? Gender.M : Gender.F;
        String className = lines[5];
        int points = Integer.parseInt(lines[6]);

        Student student = new Student(id, name, surname, date, gender, className, points);
        getStudentById().put(id, student);

        return student;
    }

    /**
     * retorna o maior ID atual entre todos os alunos
     * @return o maior ID atual entre todos os alunos
     */
    public static int getMaxId(){
        return Collections.max(getStudentById().keySet());
    }


    /**
     * Cria um novo aluno, pedindo suas informações pro usuário
     * @return objeto aluno criado baseado na entrada fornecida
     */
    public static Student createFromScratch(){
        System.out.println("Registrando novo estudante.");
        int id = getMaxId() + 1;
        System.out.println("Digite o nome.");
        String name = Tools.getString();
        System.out.println("Digite o sobrenome.");
        String surname = Tools.getString();
        System.out.println("Digite o dia do nascimento (Formato yyyy-MM-dd).");
        LocalDate birthdate = LocalDate.parse(Tools.getString());
        System.out.println("Digite o sexo (M ou F).");
        Gender gender = (Tools.getString().toLowerCase().equals("m")) ? Gender.M : Gender.F;
        System.out.println("Digite a classe.");
        String className = Tools.getString();
        int points = 0;

        Student student = new Student(id, name, surname, birthdate, gender, className, points);
        getStudentById().put(id, student);

        EventManager.getInstance().getNewStudentEvent().trigger(student);

        return student;
    }

    /**
     * Procura e retorna o ID do aluno com as informações fornecidas, ou -1 caso não seja encontrado
     * @param name nome do aluno
     * @param surname sobrenome do aluno
     * @return ID do aluno se for encontrado, ou -1 se não for encontrado
     */
    public static int findStudentId(String name, String surname){
        Iterator iterator = getStudentById().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            Student student = (Student) pair.getValue();
            if(student.getName().toLowerCase().replace(" ", "").equals(name.toLowerCase().replace(" ", "")) &&
                    student.getSurname().toLowerCase().replace(" ", "").equals(surname.toLowerCase().replace(" ", ""))) {
                return (int) pair.getKey();
            }
        }
        return -1;
    }


    public void registerBorrow(BorrowData data){
        if(data.getStudentId() != getStudentId()) return;
        getCurrentBorrows().add(data);
    }

    private void addPointsOnReturn(BorrowData data){
        int time = (int) Duration.between(data.getTakenDate(), data.getBroughtDate()).toDays();
        int points = Book.getBookById().get(data.getBookId()).getPageCount() / time;
        this.setPoints(this.getPoints() + points);
    }



    @Override
    public String formatToCSV(){
        String string = "\"" + this.getStudentId() + "\"," + "\"" + this.getName() + "\"," + "\"" + this.getSurname() + "\"," + "\"" + this.getBirthdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\"," + "\"" + getGender().toString() + "\"," + "\"" + getClassName() + "\"," + "\"" + getPoints() + "\"\n";
        return string;
    }

    @Override
    public <T> void onTrigger(EventType type, T data) {
        BorrowData borrowData = (BorrowData) data;
        switch(type) {
            case NEW_BORROW:
                if(borrowData.getStudentId() != this.getStudentId()) return;
                registerBorrow(borrowData);
                break;
            case RETURN_BORROW:
                if(borrowData.getStudentId() != this.getStudentId()) return;
                addPointsOnReturn(borrowData);
                getCurrentBorrows().remove(borrowData);
                break;

        }
    }


    //getters e setters

    public static HashMap<Integer, Student> getStudentById() {
        return studentById;
    }

    public static void setStudentById(HashMap<Integer, Student> studentById) {
        Student.studentById = studentById;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ArrayList<BorrowData> getCurrentBorrows() {
        return currentBorrows;
    }

    public void setCurrentBorrows(ArrayList<BorrowData> currentBorrows) {
        this.currentBorrows = currentBorrows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
