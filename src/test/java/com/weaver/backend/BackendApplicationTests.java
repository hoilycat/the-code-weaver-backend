package com.weaver.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
        "supabase.url=http://localhost",
        "supabase.key=test-key",
        "supabase.bucket=test-bucket",
        "admin.password=test-password",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
