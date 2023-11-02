package solomon.tests;

public class RunnableCmd implements Runnable {
    public String name;
    public Integer age;

    @Override
    public void run() {
        System.out.println(name + " " + age);
    }
}
