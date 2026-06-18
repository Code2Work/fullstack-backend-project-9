# SQL Sorguları — Northwind Mini

## Amaç
Bu projede **SQL DQL** (veri sorgulama) becerini pekiştireceksin. Küçük bir mağaza veritabanı (müşteriler, ürünler, siparişler) üzerinde 10 soruyu **saf SQL sorgularıyla** çözeceksin: `SELECT`, `WHERE`, `ORDER BY`, `LIMIT`, `DISTINCT`, `LIKE`, `IN` ve alt sorgu (subquery). **Süre: ~2-3 saat.**

> Bu projede **Java yazmıyorsun** — sadece `answers/` klasöründeki `.sql` dosyalarına SQL sorguları yazıyorsun. Testler senin sorgularını otomatik çalıştırıp sonucu kontrol eder.

## Veritabanı Şeması
```
customers
  customer_id (INT, PK)   customer_name   email   country   signup_date

products
  product_id (INT, PK)    product_name    price (NUMERIC)   stock_quantity

orders
  order_id (INT, PK)      customer_id     order_date        total_amount (NUMERIC)
```
Veri `db/schema.sql` (tablolar) + `db/data.sql` (örnek kayıtlar) dosyalarında. Testler bunları gömülü bir **H2** veritabanına yükler — senin bir şey kurmana gerek yok.

## Sorular
Her soru `answers/qNN.sql` dosyasında. Dosyanın başındaki yorum soruyu açıklar; sen sorgunu altına yazarsın.

| Dosya | Soru | Konu |
|-------|------|------|
| q01 | Tüm müşterilerin adı + ülkesi, customer_id sırasıyla | SELECT + ORDER BY |
| q02 | Türkiye'deki müşteriler | WHERE |
| q03 | Fiyatı 500'den büyük ürünler, azalan | WHERE + ORDER BY DESC |
| q04 | Tüm ürünler fiyata göre artan | ORDER BY ASC |
| q05 | En pahalı 3 ürün | ORDER BY + LIMIT |
| q06 | Farklı ülkeler, alfabetik | DISTINCT |
| q07 | Adı 'A' ile başlayan müşteriler | LIKE |
| q08 | customer_id 1, 3, 5 olan müşteriler | IN |
| q09 | Belirli bir siparişten yüksek tutarlı siparişler | subquery + karşılaştırma |
| q10 | En yüksek tutarlı siparişi veren müşteri | subquery |

## Başlamadan Önce
- **Java 17 veya 21** (LTS) + **Maven 3.9+** kurulu olmalı (testler bunlarla çalışır; ama sen SQL yazacaksın).
- SQL'i local'de denemek istersen: H17 derslerinde kurduğun **PostgreSQL** üzerinde `db/schema.sql` + `db/data.sql`'i çalıştırıp sorgularını orada test edebilirsin. Yazdığın DQL aynı şekilde çalışır.
- `answers/` dışındaki dosyalara dokunma.

## Nasıl Çözülür
1. `answers/q01.sql` dosyasını aç, soruyu oku, SQL sorgunu yorum satırının altına yaz, kaydet.
2. Sırayla q10'a kadar tüm dosyaları doldur.
3. Testleri çalıştır (aşağıda) — geçen/kalan soruları görürsün.

> 💡 Kolon adlarını şemadan birebir kullan. `ORDER BY` istenen sıralamayı verir; sıralama soruda belirtilmişse mutlaka ekle. Sayı/metin karşılaştırmaları için `=`, `>`, `LIKE`, `IN` kullan.

## Testler ve Skor
10 soru = **10 test**. Testleri çalıştırınca skorun otomatik Kaizu paneline işlenir.

**1. Öğrenci numaranı gir.** `student_id.txt` dosyasına Kaizu öğrenci numaranı yaz. (Bir kez.)

**2. Testleri çalıştır:** `mvn test` (macOS/Linux: `./run-tests.sh`, Windows: `.\run-tests.ps1`).

**3. Skor otomatik gider.** Testler bitince:
```
[Kaizu] ✅ Skorun (100/100) kaydedildi. Panelde tik birazdan görünür.
```
Her doğru sorgu bir testi yeşile çevirir; 10/10 olunca panelde **yeşil tik** gelir.

## Çözüm
Takılırsan proje sayfasındaki **"Çözümü Göster"** butonunu kullan (proje verildikten 3 gün sonra açılır). Önce kendin dene — testin verdiği "beklenen / gelen" farkı en iyi ipucu.
