package com.code2work.sqlnorthwind;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Testler bitince skoru otomatik Kaizu'ya gönderir (panelde tik).
 *
 * ⚠️ ÖĞRENCİ BU DOSYAYA DOKUNMAZ. Tek yapman gereken: student_id.txt'ye
 *    Kaizu öğrenci numaranı yazmak. Sonra `mvn test` çalıştır (ya da IDE'de
 *    tüm testleri koştur) — skorun otomatik gider.
 *
 * Bu bir JUnit "TestExecutionListener"; src/test/resources/META-INF/services
 * üzerinden Maven (surefire) tarafından otomatik kaydolur.
 */
public class C2WScoreReporter implements TestExecutionListener {

    // ── EĞİTMEN AYARLARI ───────────────────────────────
    private static final int PROJECT_ID = 737;          // bu projenin Kaizu'daki id'si (DB insert sonrası)
    private static final int EXPECTED_TOTAL = 10;     // bu projedeki toplam test sayısı
    // ───────────────────────────────────────────────────
    private static final String URL = "https://kaizu-api-8cd10af40cb3.herokuapp.com/projectLog";

    private int total = 0;
    private int passed = 0;

    @Override
    public void executionFinished(TestIdentifier id, TestExecutionResult result) {
        if (id.isTest()) {
            total++;
            if (result.getStatus() == TestExecutionResult.Status.SUCCESSFUL) {
                passed++;
            }
        }
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        if (total < EXPECTED_TOTAL) {
            System.out.println("\n[Kaizu] Kısmi çalıştırma (" + total + "/" + EXPECTED_TOTAL
                    + " test) — skor gönderilmedi. Tik için TÜM testleri çalıştır.");
            return;
        }
        int score = total > 0 ? Math.round(passed * 100f / total) : 0;

        String sid;
        try {
            sid = Files.readString(Path.of("student_id.txt")).trim();
        } catch (Exception e) {
            System.out.println("\n[Kaizu] student_id.txt bulunamadı — skor gönderilmedi. Numaranı bu dosyaya yaz.");
            return;
        }
        if (sid.isEmpty() || sid.equals("123456") || !sid.matches("\\d+")) {
            System.out.println("\n[Kaizu] student_id.txt'ye KENDİ Kaizu öğrenci numaranı yaz — skor gönderilmedi.");
            return;
        }
        if (PROJECT_ID <= 0) {
            System.out.println("\n[Kaizu] PROJECT_ID ayarlı değil — skor gönderilmedi (eğitmen ayarlayacak).");
            return;
        }

        String body = "{\"is_auto\":true,\"project_id\":" + PROJECT_ID
                + ",\"user_id\":" + sid + ",\"user_score\":" + score + "}";
        try {
            HttpResponse<String> resp = HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder(URI.create(URL))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(body))
                            .build(),
                    HttpResponse.BodyHandlers.ofString());
            int s = resp.statusCode();
            if (s == 200 || s == 201) {
                System.out.println("\n[Kaizu] ✅ Skorun (" + score + "/100) kaydedildi. Panelde tik birazdan görünür.");
            } else {
                System.out.println("\n[Kaizu] ❌ Skor gönderilemedi (HTTP " + s + "). Yanıt: " + resp.body());
            }
        } catch (Exception e) {
            System.out.println("\n[Kaizu] ❌ Skor gönderilemedi (ağ hatası): " + e.getMessage());
        }
    }
}
