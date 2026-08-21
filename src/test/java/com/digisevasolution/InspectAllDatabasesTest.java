package com.digisevasolution;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootTest
@ActiveProfiles("dev")
public class InspectAllDatabasesTest {

    private void inspectConnection(String name, String url, String user, String pass) {
        System.out.println("\n=======================================================");
        System.out.println("INSPECTING DATABASE: " + name);
        System.out.println("URL: " + url);
        System.out.println("=======================================================");

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            System.out.println("=== 1. FLYWAY SCHEMA HISTORY ===");
            try (ResultSet rs = stmt.executeQuery("SELECT installed_rank, version, description, type, script, success FROM flyway_schema_history ORDER BY installed_rank")) {
                while (rs.next()) {
                    System.out.printf("Rank: %d | Version: %s | Description: %s | Success: %b%n",
                            rs.getInt("installed_rank"),
                            rs.getString("version"),
                            rs.getString("description"),
                            rs.getBoolean("success"));
                }
            } catch (Exception e) {
                System.out.println("FAILED to query flyway_schema_history: " + e.getMessage());
            }

            System.out.println("\n=== 2. TABLES IN PUBLIC SCHEMA ===");
            try (ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name")) {
                while (rs.next()) {
                    System.out.println("Table: " + rs.getString("table_name"));
                }
            } catch (Exception e) {
                System.out.println("FAILED to query tables: " + e.getMessage());
            }

            System.out.println("\n=== 3. CATEGORIES TABLE ROW COUNT & SAMPLE ROWS ===");
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM categories")) {
                if (rs.next()) {
                    System.out.println("Categories COUNT: " + rs.getLong(1));
                }
            } catch (Exception e) {
                System.out.println("FAILED to query categories count: " + e.getMessage());
            }

            try (ResultSet rs = stmt.executeQuery("SELECT id, name_en, slug, is_active FROM categories ORDER BY id LIMIT 5")) {
                while (rs.next()) {
                    System.out.printf("ID: %d | Name: %s | Slug: %s | Active: %b%n",
                            rs.getLong("id"),
                            rs.getString("name_en"),
                            rs.getString("slug"),
                            rs.getBoolean("is_active"));
                }
            } catch (Exception e) {
                System.out.println("FAILED to query categories rows: " + e.getMessage());
            }

            System.out.println("\n=== 4. SERVICE_ITEMS CATEGORY_ID COLUMNS ===");
            try (ResultSet rs = stmt.executeQuery("SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'service_items' AND column_name IN ('category_id', 'delivery_mode', 'category')")) {
                while (rs.next()) {
                    System.out.printf("Column: %s | Type: %s%n",
                            rs.getString("column_name"),
                            rs.getString("data_type"));
                }
            } catch (Exception e) {
                System.out.println("FAILED to query service_items columns: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("COULD NOT CONNECT TO " + name + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    void testInspectBothDatabases() {
        // Database 1: ep-lively-water-a1xsacv8
        inspectConnection(
                "LIVELY_WATER_DB",
                "jdbc:postgresql://ep-lively-water-a1xsacv8-pooler.ap-southeast-1.aws.neon.tech:5432/neondb?sslmode=require",
                "neondb_owner",
                "npg_mEaN38TxlPVI"
        );

        // Database 2: ep-frosty-grass-azhucie7
        inspectConnection(
                "FROSTY_GRASS_DB",
                "jdbc:postgresql://ep-frosty-grass-azhucie7-pooler.c-3.ap-southeast-1.aws.neon.tech/digi_seva_solution?sslmode=require",
                "neondb_owner",
                "npg_x5GogXW0KkOu"
        );
    }
}
