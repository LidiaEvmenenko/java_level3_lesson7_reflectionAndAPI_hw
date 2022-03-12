package ru.geekbrains.java.hw7;
import java.time.LocalDate;
import java.util.Objects;


public class Banana {

    protected String name;
    private int weight;
    public String grade; // сорт
    private LocalDate expirationDate;// срок годности
    protected transient String deliveryCountry;// страна поставщик
    private volatile boolean permissionToSell; // разрешение на продажу

    @BeforeSuite
    public Banana() {
    }

    public Banana(String name, int weight, String grade, LocalDate expirationDate,
                  String deliveryCountry, boolean permissionToSell) {
        this.name = name;
        this.weight = weight;
        this.grade = grade;
        this.expirationDate = expirationDate;
        this.deliveryCountry = deliveryCountry;
        this.permissionToSell = permissionToSell;
    }

    @Test(priority = 2)
    private String getName() {
        return name;
    }

    @Test(priority = 2)
    private int getWeight() {
        return weight;
    }

    @Test(priority = 2)
    private String getGrade() {
        return grade;
    }

    @Test(priority = 2)
    private LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Test(priority = 2)
    private String getDeliveryCountry() {
        return deliveryCountry;
    }

    @Test(priority = 2)
    public boolean getPermissionToSell() {
        return permissionToSell;
    }

    @Test(priority = 1)
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Test(priority = 1)
    public void setPermissionToSell(boolean permissionToSell) {
        this.permissionToSell = permissionToSell;
    }

    @Test(priority = 1)
    public void setName(String name) {
        this.name = name;
    }

    @Test(priority = 1)
    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Test(priority = 1)
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Test(priority = 1)
    public void setDeliveryCountry(String deliveryCountry) {
        this.deliveryCountry = deliveryCountry;
    }

    @AfterSuite
    @Override
    public String toString() {
        return "Banana{" +
                "Имя='" + name + '\'' +
                ", Вес=" + weight +
                ", Сорт='" + grade + '\'' +
                ", Срок годности=" + expirationDate +
                ", Страна производитель='" + deliveryCountry + '\'' +
                ", Разрешение на продажу=" + permissionToSell +
                '}';
    }

    @Test(priority = 5)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Banana)) return false;
        Banana banana = (Banana) o;
        return getWeight() == banana.getWeight() && getPermissionToSell() == banana.getPermissionToSell() &&
                getName().equals(banana.getName()) && getGrade().equals(banana.getGrade()) &&
                getExpirationDate().equals(banana.getExpirationDate()) &&
                getDeliveryCountry().equals(banana.getDeliveryCountry());
    }

    @Test(priority = 6)
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getWeight(), getGrade(), getExpirationDate(), getDeliveryCountry(), getPermissionToSell());
    }

    @Test(priority = 7)
    public int addWeight(int weight) {
        this.weight += weight;
        return this.weight;
    }

    @Test(priority = 4)
    public int removeWeight(int weight) {
        if ((this.weight - weight) < 0) {
            throw new RuntimeException("Некорректное значение заданного веса.");
        }
        this.weight -= weight;
        return this.weight;
    }

    @Test(priority = 3)
    public LocalDate updateExpirationDate(long days) {
        expirationDate = expirationDate.plusDays(days);
        return expirationDate;
    }

}
