package com.digisevasolution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("dev")
public class InspectDatabaseStatusTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void inspectDatabase() {
        System.out.println("=== 1. FLYWAY SCHEMA HISTORY ===");
        try {
            List<Map<String, Object>> flywayRows = jdbcTemplate.queryForList(
                    "SELECT installed_rank, version, description, type, script, success FROM flyway_schema_history ORDER BY installed_rank"
            );
            for (Map<String, Object> row : flywayRows) {
                System.out.println(row);
            }
        } catch (Exception e) {
            System.out.println("Error querying flyway_schema_history: " + e.getMessage());
        }

        System.out.println("\n=== 2. TABLES IN PUBLIC SCHEMA ===");
        try {
            List<Map<String, Object>> tables = jdbcTemplate.queryForList(
                    "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name"
            );
            for (Map<String, Object> table : tables) {
                System.out.println(table.get("table_name"));
            }
        } catch (Exception e) {
            System.out.println("Error querying tables: " + e.getMessage());
        }

        System.out.println("\n=== 3. CATEGORIES TABLE COUNT AND ROWS ===");
        try {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categories", Long.class);
            System.out.println("Categories total count: " + count);
            List<Map<String, Object>> catRows = jdbcTemplate.queryForList(
                    "SELECT id, name_en, slug, display_order, is_active FROM categories ORDER BY id"
            );
            for (Map<String, Object> row : catRows) {
                System.out.println(row);
            }
        } catch (Exception e) {
            System.out.println("Error querying categories: " + e.getMessage());
        }

        System.out.println("\n=== 4. SERVICE_ITEMS COLUMNS AND CATEGORY_ID FK ===");
        try {
            List<Map<String, Object>> cols = jdbcTemplate.queryForList(
                    "SELECT column_name, data_type, is_nullable FROM information_schema.columns WHERE table_name = 'service_items' ORDER BY ordinal_position"
            );
            for (Map<String, Object> col : cols) {
                System.out.println(col);
            }
        } catch (Exception e) {
            System.out.println("Error querying service_items columns: " + e.getMessage());
        }
    }
}
