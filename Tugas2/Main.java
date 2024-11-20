import java.util.*;

class Menu {
    String name;
    double price;
    String category;

    public Menu(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }
}

public class Main {
    private static List<Menu> menuItems = new ArrayList<>(Arrays.asList(
        new Menu("Nasi Goreng", 25000, "makanan"),
        new Menu("Mie Goreng", 20000, "makanan"),
        new Menu("Kentang Goreng", 15000, "makanan"),
        new Menu("Ayam Penyet", 30000, "makanan"),
        new Menu("Kopi", 10000, "minuman"),
        new Menu("Teh Manis", 5000, "minuman"),
        new Menu("Es Jeruk", 8000, "minuman"),
        new Menu("Jus Buah", 12000, "minuman")
    ));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("=== Restoran anamBadrus ===");
            System.out.println("1. Menu Pelanggan");
            System.out.println("2. Manajemen Menu");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine(); // Clear newline

            switch (pilihan) {
                case 1:
                    menuPelanggan(scanner);
                    break;
                case 2:
                    manajemenMenu(scanner);
                    break;
                case 0:
                    System.out.println("Terima kasih telah menggunakan aplikasi!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // === MENU PELANGGAN ===
    private static void menuPelanggan(Scanner scanner) {
        Map<String, Integer> pesanan = new HashMap<>();
        tampilkanMenu();

        System.out.println("Masukkan pesanan Anda (ketik 'selesai' untuk mengakhiri):");
        while (true) {
            System.out.print("Nama Menu = Jumlah: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("selesai")) break;

            String[] parts = input.split("=");
            if (parts.length == 2) {
                String menuName = parts[0].trim();
                try {
                    int quantity = Integer.parseInt(parts[1].trim());
                    if (quantity > 0 && isMenuValid(menuName)) {
                        pesanan.put(menuName, pesanan.getOrDefault(menuName, 0) + quantity);
                    } else {
                        System.out.println("Menu tidak valid atau jumlah tidak sesuai.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Jumlah tidak valid.");
                }
            } else {
                System.out.println("Format tidak sesuai. Gunakan: NamaMenu = Jumlah");
            }
        }

        if (!pesanan.isEmpty()) {
            double[] totalBiayaData = hitungTotalBiaya(pesanan);
            cetakStruk(pesanan, totalBiayaData);
        } else {
            System.out.println("Tidak ada pesanan.");
        }
    }

    // === MANAJEMEN MENU ===
    private static void manajemenMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Manajemen Menu ===");
            System.out.println("1. Tambah Menu");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("0. Kembali");
            System.out.print("Pilih menu: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine(); // Clear newline

            switch (pilihan) {
                case 1:
                    tambahMenu(scanner);
                    break;
                case 2:
                    ubahHargaMenu(scanner);
                    break;
                case 3:
                    hapusMenu(scanner);
                    break;
                case 0:
                    System.out.println("Kembali ke menu utama...");
                    return; 
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void tambahMenu(Scanner scanner) {
        System.out.println("\n=== Tambah Menu ===");
        System.out.print("Nama Menu: ");
        String name = scanner.nextLine();
        System.out.print("Harga Menu: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Clear newline
        System.out.print("Kategori (makanan/minuman): ");
        String category = scanner.nextLine().toLowerCase();
    
        if (!category.equals("makanan") && !category.equals("minuman")) {
            System.out.println("Kategori tidak valid.");
            return;
        }
    
        menuItems.add(new Menu(name, price, category));
    
        // Sorting agar kategori tetap terkelompok
        menuItems.sort(Comparator.comparing((Menu m) -> m.category).thenComparing(m -> m.name));
        System.out.println("Menu berhasil ditambahkan.");
    }

    private static void ubahHargaMenu(Scanner scanner) {
        tampilkanMenu();
        System.out.print("Nomor menu yang ingin diubah: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // Clear newline

        if (index >= 0 && index < menuItems.size()) {
            System.out.print("Harga baru: ");
            double newPrice = scanner.nextDouble();
            scanner.nextLine(); // Clear newline
            menuItems.get(index).price = newPrice;
            System.out.println("Harga berhasil diubah.");
        } else {
            System.out.println("Menu tidak ditemukan.");
        }
    }

    private static void hapusMenu(Scanner scanner) {
        tampilkanMenu();
        System.out.print("Nomor menu yang ingin dihapus: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // Clear newline
    
        if (index >= 0 && index < menuItems.size()) {
            System.out.print("Apakah Anda yakin ingin menghapus menu ini? (Ya/Tidak): ");
            String konfirmasi = scanner.nextLine();
            if (konfirmasi.equalsIgnoreCase("Ya")) {
                menuItems.remove(index);
    
                // Sorting agar kategori tetap terkelompok
                menuItems.sort(Comparator.comparing((Menu m) -> m.category).thenComparing(m -> m.name));
                System.out.println("Menu berhasil dihapus.");
            } else {
                System.out.println("Menu tidak dihapus.");
            }
        } else {
            System.out.println("Menu tidak ditemukan.");
        }
    }

    // === FUNGSI UTILITAS ===
    private static void tampilkanMenu() {
        System.out.println("\n=== Daftar Menu ===");
    
        // Pisahkan menu berdasarkan kategori
        System.out.println("\nKategori: Makanan");
        int nomor = 1;
        for (Menu item : menuItems) {
            if (item.category.equalsIgnoreCase("makanan")) {
                System.out.printf("%d. %s (Rp %.2f)\n", nomor++, item.name, item.price);
            }
        }
    
        System.out.println("\nKategori: Minuman");
        for (Menu item : menuItems) {
            if (item.category.equalsIgnoreCase("minuman")) {
                System.out.printf("%d. %s (Rp %.2f)\n", nomor++, item.name, item.price);
            }
        }
    }
    

    private static boolean isMenuValid(String menuName) {
        return menuItems.stream().anyMatch(item -> item.name.equalsIgnoreCase(menuName));
    }

    private static double getMenuPrice(String menuName) {
        return menuItems.stream().filter(item -> item.name.equalsIgnoreCase(menuName)).findFirst().orElse(new Menu("", 0, "")).price;
    }

    private static double[] hitungTotalBiaya(Map<String, Integer> pesanan) {
        double totalBiaya = 0, tax, discount = 0, serviceCharge = 20000;

        for (String item : pesanan.keySet()) {
            totalBiaya += getMenuPrice(item) * pesanan.get(item);
        }

        if (totalBiaya > 100000) discount = totalBiaya * 0.1;
        tax = (totalBiaya - discount) * 0.1;

        return new double[]{totalBiaya - discount + tax + serviceCharge, discount, tax};
    }

    private static void cetakStruk(Map<String, Integer> pesanan, double[] totalBiayaData) {
        System.out.println("\n=== STRUK PEMESANAN ===");
        System.out.println("----------------------------------------");
        pesanan.forEach((item, quantity) -> {
            double price = getMenuPrice(item);
            System.out.printf("%-20s x %d = Rp. %.2f\n", item, quantity, price * quantity);
        });

        double totalBiaya = totalBiayaData[0];
        double discount = totalBiayaData[1];
        double tax = totalBiayaData[2];

        if (discount > 0) System.out.printf("Diskon (10%%): Rp. %.2f\n", discount);
        System.out.printf("Pajak (10%%): Rp. %.2f\n", tax);
        System.out.printf("Biaya Pelayanan: Rp. %.2f\n", 20000.0);
        System.out.printf("----------------------------------------\n");
        System.out.printf("Total Biaya: Rp. %.2f\n", totalBiaya);
    }
}
