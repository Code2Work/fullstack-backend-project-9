package com.code2work.sqlnorthwind;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Öğrencinin answers/qNN.sql dosyalarındaki SQL sorgularını gömülü H2 veritabanında
 * çalıştırır ve sonuçları beklenenle karşılaştırır.
 *
 * ⚠️ ÖĞRENCİ BU DOSYAYA DOKUNMAZ. Sadece answers/*.sql dosyalarına SQL yazar.
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
@DisplayName("SQL Northwind soruları")
class SqlQuestionsTest {

    private static Connection conn;

    @BeforeAll
    static void setUp() throws Exception {
        conn = DriverManager.getConnection("jdbc:h2:mem:northwind;DB_CLOSE_DELAY=-1");
        runScript("db/schema.sql");
        runScript("db/data.sql");
    }

    @AfterAll
    static void tearDown() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    /** Bir .sql script dosyasını (; ile ayrılmış) çalıştırır. */
    static void runScript(String path) throws Exception {
        String sql = Files.readString(Path.of(path));
        for (String stmt : sql.split(";")) {
            String s = stmt.trim();
            if (s.isEmpty()) {
                continue;
            }
            try (Statement st = conn.createStatement()) {
                st.execute(s);
            }
        }
    }

    /** answers/<file> içindeki SQL sorgusunu çalıştırır; yorum satırlarını (--) atlar. */
    private List<List<String>> runAnswer(String file) throws Exception {
        String raw = Files.readString(Path.of("answers/" + file));
        StringBuilder sb = new StringBuilder();
        for (String line : raw.split("\n")) {
            String t = line.trim();
            if (t.startsWith("--") || t.isEmpty()) {
                continue;
            }
            sb.append(line).append("\n");
        }
        String query = sb.toString().trim();
        if (query.endsWith(";")) {
            query = query.substring(0, query.length() - 1);
        }
        assertFalse(query.isEmpty(), file + " boş görünüyor — SQL sorgunu bu dosyaya yaz.");

        List<List<String>> rows = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
            int cols = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= cols; i++) {
                    row.add(rs.getString(i));
                }
                rows.add(row);
            }
        }
        return rows;
    }

    private void check(String file, String[][] expected) throws Exception {
        List<List<String>> actual = runAnswer(file);
        assertEquals(expected.length, actual.size(),
                file + ": beklenen " + expected.length + " satır, gelen " + actual.size());
        for (int r = 0; r < expected.length; r++) {
            String[] exp = expected[r];
            List<String> act = actual.get(r);
            assertEquals(exp.length, act.size(), file + " satır " + (r + 1) + ": kolon sayısı uyuşmuyor");
            for (int c = 0; c < exp.length; c++) {
                assertTrue(cellEquals(exp[c], act.get(c)),
                        file + " satır " + (r + 1) + " kolon " + (c + 1)
                                + ": beklenen '" + exp[c] + "', gelen '" + act.get(c) + "'");
            }
        }
    }

    /** Sayısal hücreleri double olarak karşılaştırır (1899 == 1899.00); değilse string eşitliği. */
    private boolean cellEquals(String expected, String actual) {
        if (Objects.equals(expected, actual)) {
            return true;
        }
        if (expected == null || actual == null) {
            return false;
        }
        try {
            return Math.abs(Double.parseDouble(expected) - Double.parseDouble(actual)) < 0.001;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Test
    @DisplayName("q01: müşteri adı + ülke, customer_id sırasıyla")
    void q01() throws Exception {
        check("q01.sql", new String[][]{
                {"Ali Veli", "Turkey"}, {"Ayşe Yılmaz", "Turkey"}, {"John Doe", "USA"},
                {"Emma Brown", "UK"}, {"Carlos Mendez", "Mexico"}, {"Merve Demir", "Turkey"}});
    }

    @Test
    @DisplayName("q02: Türkiye'deki müşteriler (WHERE)")
    void q02() throws Exception {
        check("q02.sql", new String[][]{{"Ali Veli"}, {"Ayşe Yılmaz"}, {"Merve Demir"}});
    }

    @Test
    @DisplayName("q03: fiyatı 500'den büyük ürünler, azalan (WHERE + ORDER BY DESC)")
    void q03() throws Exception {
        check("q03.sql", new String[][]{
                {"27\" Monitor", "1899"}, {"Noise-Cancelling Headphones", "1250.75"}});
    }

    @Test
    @DisplayName("q04: tüm ürünler fiyata göre artan (ORDER BY ASC)")
    void q04() throws Exception {
        check("q04.sql", new String[][]{
                {"USB-C Cable", "49.95"}, {"Wireless Mouse", "199.9"}, {"Laptop Stand", "329.5"},
                {"Gaming Keyboard", "499.99"}, {"Noise-Cancelling Headphones", "1250.75"}, {"27\" Monitor", "1899"}});
    }

    @Test
    @DisplayName("q05: en pahalı 3 ürün (ORDER BY DESC + LIMIT)")
    void q05() throws Exception {
        check("q05.sql", new String[][]{
                {"27\" Monitor", "1899"}, {"Noise-Cancelling Headphones", "1250.75"}, {"Gaming Keyboard", "499.99"}});
    }

    @Test
    @DisplayName("q06: farklı ülkeler, alfabetik (DISTINCT + ORDER BY)")
    void q06() throws Exception {
        check("q06.sql", new String[][]{{"Mexico"}, {"Turkey"}, {"UK"}, {"USA"}});
    }

    @Test
    @DisplayName("q07: adı A ile başlayan müşteriler (LIKE)")
    void q07() throws Exception {
        check("q07.sql", new String[][]{{"Ali Veli"}, {"Ayşe Yılmaz"}});
    }

    @Test
    @DisplayName("q08: customer_id 1,3,5 (IN)")
    void q08() throws Exception {
        check("q08.sql", new String[][]{{"Ali Veli"}, {"John Doe"}, {"Carlos Mendez"}});
    }

    @Test
    @DisplayName("q09: belirli bir siparişten yüksek tutarlı siparişler (subquery + karşılaştırma)")
    void q09() throws Exception {
        check("q09.sql", new String[][]{{"6", "1899"}, {"3", "1250.75"}, {"2", "499.99"}});
    }

    @Test
    @DisplayName("q10: en yüksek tutarlı siparişi veren müşteri (subquery)")
    void q10() throws Exception {
        check("q10.sql", new String[][]{{"Carlos Mendez"}});
    }
}
