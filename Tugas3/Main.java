import java.io.*;
import java.util.*;

// === Kelas Abstrak MenuItem ===
abstract class MenuItem {
    private final String nama;
    private double harga;
    private final String kategori;

    public MenuItem(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public abstract void tampilMenu();
}

// === Kelas Turunan Makanan ===
class Makanan extends MenuItem {
    private final String jenisMakanan;

    public Makanan(String nama, double harga, String jenisMakanan) {
        super(nama, harga, "Makanan");
        this.jenisMakanan = jenisMakanan;
    }

    public String getJenisMakanan() {
        return jenisMakanan;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("%-20s Rp %.2f [%s]\n", getNama(), getHarga(), jenisMakanan);
    }
}

// === Kelas Turunan Minuman ===
class Minuman extends MenuItem {
    private final String jenisMinuman;

    public Minuman(String nama, double harga, String jenisMinuman) {
        super(nama, harga, "Minuman");
        this.jenisMinuman = jenisMinuman;
    }

    public String getJenisMinuman() {
        return jenisMinuman;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("%-20s Rp %.2f [%s]\n", getNama(), getHarga(), jenisMinuman);
    }
}

// === Kelas Turunan Diskon ===
class Diskon extends MenuItem {
    private final double persentaseDiskon;

    public Diskon(String nama, double persentaseDiskon) {
        super(nama, 0, "Diskon");
        this.persentaseDiskon = persentaseDiskon;
    }

    public double getPersentaseDiskon() {
        return persentaseDiskon;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("%-20s %.0f%% Diskon\n", getNama(), persentaseDiskon * 100);
    }
}

// === Kelas Menu untuk Mengelola Item ===
class Menu {
    private final List<MenuItem> daftarMenu;

    public Menu() {
        this.daftarMenu = new ArrayList<>();
    }

    public void tambahMenu(MenuItem item) {
        daftarMenu.add(item);
        System.out.println("Item berhasil ditambahkan ke menu.");
    }

    public void tampilkanMenu() {
        System.out.println("\n=== Daftar Menu ===");
        daftarMenu.forEach(MenuItem::tampilMenu);
    }

    public MenuItem cariMenu(String nama) {
        return daftarMenu.stream()
                .filter(item -> item.getNama().equalsIgnoreCase(nama))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Item tidak ditemukan: " + nama));
    }

    public void simpanKeFile(String namaFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile))) {
            for (MenuItem item : daftarMenu) {
                writer.write(item.getNama() + "," + item.getHarga() + "," + item.getKategori() + "\n");
            }
        }
    }

    public void muatDariFile(String namaFile) throws IOException {
        daftarMenu.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(namaFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String nama = parts[0];
                double harga = Double.parseDouble(parts[1]);
                String kategori = parts[2];

                if (kategori.equalsIgnoreCase("Makanan")) {
                    daftarMenu.add(new Makanan(nama, harga, "Umum"));
                } else if (kategori.equalsIgnoreCase("Minuman")) {
                    daftarMenu.add(new Minuman(nama, harga, "Umum"));
                } else if (kategori.equalsIgnoreCase("Diskon")) {
                    daftarMenu.add(new Diskon(nama, harga));
                }
            }
        }
    }
}

// === Kelas Pesanan untuk Mengelola Pesanan ===
class Pesanan {
    private final Map<MenuItem, Integer> pesanan;

    public Pesanan() {
        this.pesanan = new HashMap<>();
    }

    public void tambahPesanan(MenuItem item, int jumlah) {
        pesanan.put(item, pesanan.getOrDefault(item, 0) + jumlah);
    }

    public void cetakStruk(String namaFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile))) {
            double total = 0;
            writer.write("=== STRUK PEMESANAN ===\n");
            writer.write("------------------------\n");
            for (Map.Entry<MenuItem, Integer> entry : pesanan.entrySet()) {
                MenuItem item = entry.getKey();
                int jumlah = entry.getValue();
                double hargaTotal = item.getHarga() * jumlah;
                writer.write(String.format("%-20s x %d = Rp %.2f\n", item.getNama(), jumlah, hargaTotal));
                total += hargaTotal;
            }
            writer.write("------------------------\n");
            writer.write(String.format("Total Biaya: Rp %.2f\n", total));
        }
    }
}

// === Program Utama ===
public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        Scanner scanner = new Scanner(System.in);

        try {
            menu.muatDariFile("menu.txt");
        } catch (IOException e) {
            System.out.println("Menu gagal dimuat. Menggunakan menu kosong.");
        }

        while (true) {
            System.out.println("\n=== Restoran anamBadrus ===");
            System.out.println("1. Menu Customer");
            System.out.println("2. Manajemen Menu");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1 -> customerMenu(menu, scanner);
                case 2 -> manajemenMenu(menu, scanner);
                case 0 -> {
                    System.out.println("Terima kasih!");
                    return;
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void customerMenu(Menu menu, Scanner scanner) {
        Pesanan pesanan = new Pesanan();
        System.out.println("\n=== Menu Customer ===");
        menu.tampilkanMenu();
        while (true) {
            System.out.print("Nama menu (ketik 'selesai' untuk keluar): ");
            String nama = scanner.nextLine();
            if (nama.equalsIgnoreCase("selesai")) break;
            try {
                MenuItem item = menu.cariMenu(nama);
                System.out.print("Jumlah: ");
                int jumlah = scanner.nextInt();
                scanner.nextLine(); // Membersihkan buffer
                pesanan.tambahPesanan(item, jumlah);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        try {
            pesanan.cetakStruk("struk_customer.txt");
            System.out.println("Struk disimpan di 'struk_customer.txt'.");
        } catch (IOException e) {
            System.out.println("Gagal mencetak struk: " + e.getMessage());
        }
    
        // Tambahkan pesan transisi
        System.out.println("Pesanan selesai. Kembali ke menu utama...");
    }
    
    private static void manajemenMenu(Menu menu, Scanner scanner) {
        System.out.println("\n=== Manajemen Menu ===");
        System.out.print("Tambah item (makanan/minuman/diskon): ");
        String jenis = scanner.nextLine();
        System.out.print("Nama: ");
        String nama = scanner.nextLine();
        System.out.print("Harga: ");
        double harga = scanner.nextDouble();
        scanner.nextLine();
        if (jenis.equalsIgnoreCase("makanan")) {
            menu.tambahMenu(new Makanan(nama, harga, "Umum"));
        } else if (jenis.equalsIgnoreCase("minuman")) {
            menu.tambahMenu(new Minuman(nama, harga, "Umum"));
        } else if (jenis.equalsIgnoreCase("diskon")) {
            menu.tambahMenu(new Diskon(nama, harga / 100));
        }
        try {
            menu.simpanKeFile("menu.txt");
            System.out.println("Menu disimpan di 'menu.txt'.");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan menu: " + e.getMessage());
        }
    }
}
