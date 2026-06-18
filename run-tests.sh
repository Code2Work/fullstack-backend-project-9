#!/usr/bin/env bash
# SQL Northwind — testleri çalıştırır (mvn). Skoru Kaizu'ya gönderen kısım
# JUnit listener'dadır (src/test/.../C2WScoreReporter.java) — mvn test sonunda otomatik POST eder.
echo "→ Testler çalışıyor (mvn test)..."
mvn test
