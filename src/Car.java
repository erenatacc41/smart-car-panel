public class Car {
    // Encapsulation: Değişkenler private (Gizli)
    private final String brand;
    private final String model;
    private final int year;

    // Constructor (Kurucu Metot)
    public Car(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    // Getter Metotları (Verilere erişmek için)
    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }
}