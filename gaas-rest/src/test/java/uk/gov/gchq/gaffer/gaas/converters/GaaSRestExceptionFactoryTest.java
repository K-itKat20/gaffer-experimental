/*
 * Copyright 2020 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.gaffer.gaas.converters;

import io.kubernetes.client.openapi.ApiException;
import org.junit.jupiter.api.Test;
import uk.gov.gchq.gaffer.gaas.exception.GaaSRestApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.gchq.gaffer.gaas.utilities.ApiExceptionTestFactory.makeApiException_custom;
import static uk.gov.gchq.gaffer.gaas.utilities.ApiExceptionTestFactory.makeApiException_duplicateGraph;
import static uk.gov.gchq.gaffer.gaas.utilities.ApiExceptionTestFactory.makeApiException_timeout;

public class GaaSRestExceptionFactoryTest {

    @Test
    public void convertApiExceptionWhenResponseBodyIsNotJson() {
        final ApiException apiException = makeApiException_custom("null");

        final GaaSRestApiException actual = GaaSRestExceptionFactory.from(apiException);

        assertEquals("UnknownError", actual.getMessage());
        assertEquals("null", actual.getBody());
        assertEquals(0, actual.getStatusCode());
        assertTrue(actual.getCause() instanceof ApiException);
    }

    @Test
    public void convertAlreadyExistsApiExceptionToGaasApiException() {
        final ApiException apiException = makeApiException_duplicateGraph();

        final GaaSRestApiException actual = GaaSRestExceptionFactory.from(apiException);

        assertEquals("AlreadyExists", actual.getMessage());
        assertEquals("gaffers.gchq.gov.uk \"testgraphid\" already exists", actual.getBody());
        assertEquals(409, actual.getStatusCode());
        assertTrue(actual.getCause() instanceof ApiException);
    }

    @Test
    public void convertApiExceptionToGaasApiException() {
        final ApiException apiException = makeApiException_timeout();

        final GaaSRestApiException actual = GaaSRestExceptionFactory.from(apiException);

        assertEquals("java.net.SocketTimeoutException: connect timed out", actual.getMessage());
        assertEquals(null, actual.getBody());
        assertEquals(0, actual.getStatusCode());
        assertTrue(actual.getCause() instanceof ApiException);
    }

}
