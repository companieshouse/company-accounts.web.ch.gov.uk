package uk.gov.companieshouse.web.accounts.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("OpenTelemetryAppenderInitializer Tests")
class OpenTelemetryAppenderInitializerTest {

    @Mock
    private OpenTelemetry mockOpenTelemetry;

    @Test
    @DisplayName("Should construct successfully with OpenTelemetry dependency")
    void testConstructorWithOpenTelemetryDependency() {
        OpenTelemetryAppenderInitializer initializer = new OpenTelemetryAppenderInitializer(
            mockOpenTelemetry);

        assertNotNull(initializer, "Initializer should be constructed successfully");
    }

    @Test
    @DisplayName("Should install OpenTelemetryAppender when afterPropertiesSet is called")
    void testAfterPropertiesSetInstallsAppender() {
        try (var appenderMock = mockStatic(OpenTelemetryAppender.class)) {
            OpenTelemetryAppenderInitializer initializer = new OpenTelemetryAppenderInitializer(
                mockOpenTelemetry);

            initializer.afterPropertiesSet();

            appenderMock.verify(() -> OpenTelemetryAppender.install(mockOpenTelemetry));
        }
    }

    @Test
    @DisplayName("Should handle multiple afterPropertiesSet calls gracefully")
    void testMultipleAfterPropertiesSetCalls() {
        try (var appenderMock = mockStatic(OpenTelemetryAppender.class)) {
            OpenTelemetryAppenderInitializer initializer = new OpenTelemetryAppenderInitializer(
                mockOpenTelemetry);

            initializer.afterPropertiesSet();
            initializer.afterPropertiesSet();

            appenderMock.verify(() -> OpenTelemetryAppender.install(mockOpenTelemetry),
                times(2));
        }
    }
}




