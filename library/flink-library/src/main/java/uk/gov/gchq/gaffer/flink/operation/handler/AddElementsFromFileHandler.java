/*
 * Copyright 2017 Crown Copyright
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
package uk.gov.gchq.gaffer.flink.operation.handler;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import uk.gov.gchq.gaffer.flink.operation.AddElementsFromFile;
import uk.gov.gchq.gaffer.flink.operation.utils.FlinkOptions;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.store.Context;
import uk.gov.gchq.gaffer.store.Store;
import uk.gov.gchq.gaffer.store.operation.handler.OperationHandler;

import java.util.Map;

public class AddElementsFromFileHandler implements OperationHandler<AddElementsFromFile> {
    @Override
    public Object doOperation(final AddElementsFromFile operation, final Context context, final Store store) throws OperationException {

        final Map<String, String> options = validateOperation(operation);

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.readTextFile(operation.getFilename())
                .map(new GafferMapFunction(operation.getElementGenerator()))
                .returns(GafferMapFunction.getReturnClass())
                .addSink(new GafferSink(store));

        try {
            env.execute(options.get(FlinkOptions.FLINK_JOB_NAME));
        } catch (Exception e) {
            throw new OperationException("Failed to add elements from kafta topic: " + operation.getFilename(), e);
        }

        return null;    }

    private Map<String, String> validateOperation(final AddElementsFromFile operation) {

        final Map<String, String> options = operation.getOptions();

        if (!options.containsKey(FlinkOptions.FLINK_JOB_NAME)) {
            throw new IllegalArgumentException("Unable to build AddElementsFromFile operation - "
                    + FlinkOptions.FLINK_JOB_NAME + " is not set");
        }

        return options;
    }
}
