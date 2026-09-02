import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CarManagementSystem extends JFrame {

    // Veritabanı Bilgileri (Kendi şifrenizi buraya yazın)
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/smart_drive";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "Eren9696";

    // Arayüz Bileşenleri
    private JTextField txtBrand;
    private JTextField txtModel;
    private JTextField txtYear;
    private JButton btnRegister;

    public CarManagementSystem() {
        // Pencere Ayarları
        setTitle("SmartDrive Rentals - Car Registration");
        setSize(450, 400); // Pencereyi biraz büyüttük
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Ortada açılsın

        // Ana Panel (Arkaplan ve Kenar Boşlukları)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Kenarlardan 20px boşluk
        mainPanel.setBackground(new Color(245, 245, 245)); // Açık gri arka plan
        add(mainPanel);

        // --- 1. BAŞLIK KISMI ---
        JLabel titleLabel = new JLabel("Car Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 150)); // Koyu Mavi
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- 2. FORM ALANI (ORTA) ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 15)); // 3 satır, 2 sütun, boşluklu
        formPanel.setOpaque(false); // Arka plan rengini ana panelden alsın

        // Font Ayarları
        Font labelFont = new Font("SansSerif", Font.PLAIN, 14);
        Font inputFont = new Font("SansSerif", Font.PLAIN, 14);

        // Marka
        JLabel lblBrand = new JLabel("Car Brand:");
        lblBrand.setFont(labelFont);
        txtBrand = new JTextField();
        txtBrand.setFont(inputFont);
        formPanel.add(lblBrand);
        formPanel.add(txtBrand);

        // Model
        JLabel lblModel = new JLabel("Car Model:");
        lblModel.setFont(labelFont);
        txtModel = new JTextField();
        txtModel.setFont(inputFont);
        formPanel.add(lblModel);
        formPanel.add(txtModel);

        // Yıl
        JLabel lblYear = new JLabel("Manufacture Year:");
        lblYear.setFont(labelFont);
        txtYear = new JTextField();
        txtYear.setFont(inputFont);
        formPanel.add(lblYear);
        formPanel.add(txtYear);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- 3. BUTON KISMI (ALT) ---
        btnRegister = new JButton("REGISTER CAR");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnRegister.setBackground(new Color(50, 150, 50)); // Yeşil Buton
        btnRegister.setForeground(Color.WHITE); // Beyaz Yazı
        btnRegister.setFocusPainted(false); // Tıklayınca oluşan çirkin çerçeveyi kaldır
        btnRegister.setPreferredSize(new Dimension(140, 50)); // Buton yüksekliği

        // Buton Paneli (Butonun çok genişlememesi için)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.add(btnRegister);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Buton Aksiyonu
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerCarProcess();
            }
        });

        setVisible(true);
    }

    // --- MANTIK KISMI (AYNI KALDI) ---
    private void registerCarProcess() {
        String brand = txtBrand.getText();
        String model = txtModel.getText();
        String yearText = txtYear.getText();

        if (brand.isEmpty() || model.isEmpty() || yearText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int year = Integer.parseInt(yearText);
            Car newCar = new Car(brand, model, year);
            saveCarToDatabase(newCar);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Year! Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCarToDatabase(Car car) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO cars (brand, model, year) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "SUCCESS!\nCar added to database:\n" + car.getBrand() + " " + car.getModel());
                txtBrand.setText("");
                txtModel.setText("");
                txtYear.setText("");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        // MODERN GÖRÜNÜMÜ (NIMBUS) AKTİF ETME
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Nimbus yoksa standart görünümle devam et
        }

        // Uygulamayı başlat
        SwingUtilities.invokeLater(() -> new CarManagementSystem());
    }
}