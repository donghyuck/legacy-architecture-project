/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.common.adaptor.processor;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.DataProcessor;
import architecture.common.adaptor.ReadConnector;
import architecture.common.adaptor.WriteConnector;

/**
 * 
 * @author donghyuck
 */
public class DataSyncProcessor implements DataProcessor {

    private ReadConnector readConnector;

    private WriteConnector writeConnector;

    public Object process(Object... args) {

	Context context = getContext();

	Object data = getReadConnector().pull(context);

	context.setObject("data", data);

	return getWriteConnector().deliver(context);
    }

    public Context getContext() {
	return null;
    }

    public ReadConnector getReadConnector() {
	return readConnector;
    }

    public void setReadConnector(ReadConnector readConnector) {
	this.readConnector = readConnector;
    }

    public WriteConnector getWriteConnector() {
	return writeConnector;
    }

    public void setWriteConnector(WriteConnector writeConnector) {
	this.writeConnector = writeConnector;
    }

}
