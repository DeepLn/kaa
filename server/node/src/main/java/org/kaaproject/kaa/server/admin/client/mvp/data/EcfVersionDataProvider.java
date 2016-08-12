/*
 * Copyright 2014-2016 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaaproject.kaa.server.admin.client.mvp.data;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.kaaproject.avro.ui.gwt.client.widget.grid.AbstractGrid;
import org.kaaproject.kaa.common.dto.ctl.CTLSchemaMetaInfoDto;
import org.kaaproject.kaa.common.dto.event.EventClassDto;
import org.kaaproject.kaa.server.admin.client.KaaAdmin;
import org.kaaproject.kaa.server.admin.client.mvp.activity.grid.AbstractDataProvider;
import org.kaaproject.kaa.server.admin.client.util.HasErrorMessage;
import org.kaaproject.kaa.server.admin.shared.schema.EventClassViewDto;
import org.kaaproject.kaa.server.common.dao.model.sql.CTLSchemaMetaInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EcfVersionDataProvider extends AbstractDataProvider<EventClassDto, String> {

    private String eventClassFamilyId;
    private int version;
    private List<EventClassViewDto> eventClassViewDtoList;

    public EcfVersionDataProvider(AbstractGrid<EventClassDto, String> dataGrid, HasErrorMessage hasErrorMessage, String eventClassFamilyId,
                                  int version, List<EventClassViewDto> eventClassViewDtoList) {
        super(dataGrid, hasErrorMessage, false);
        this.eventClassFamilyId = eventClassFamilyId;
        this.version = version;
        this.eventClassViewDtoList = eventClassViewDtoList;
        addDataDisplay();
    }

    @Override
    protected void loadData(final LoadCallback callback) {

        if (version > 0) {
            KaaAdmin.getDataSource().getEventClassesByFamilyIdVersionAndType(eventClassFamilyId, version, null,
                    new AsyncCallback<List<EventClassDto>>() {
                        @Override
                        public void onFailure(Throwable cause) {
                            callback.onFailure(cause);
                        }

                        @Override
                        public void onSuccess(List<EventClassDto> result) {
                            callback.onSuccess(result);
                        }
                    });
        }
        List<EventClassDto> eventClassDtoList = null;
        if (eventClassViewDtoList != null) {
            eventClassDtoList = new ArrayList<EventClassDto>();
            int i = 1;
            for (EventClassViewDto eventClassViewDto : eventClassViewDtoList) {
                if (eventClassViewDto.getSchema() != null) {
                    CTLSchemaMetaInfoDto ctlSchemaMetaInfoDto = eventClassViewDto.getExistingMetaInfo().getMetaInfo();

                    EventClassDto eventClassDto = eventClassViewDto.getSchema();
                    eventClassDto.setCtlSchemaId(ctlSchemaMetaInfoDto.getId());
                    eventClassDto.setFqn(ctlSchemaMetaInfoDto.getFqn());
                    eventClassDto.setId(String.valueOf(i++));

                    eventClassDtoList.add(eventClassDto);

                }
            }
            callback.onSuccess(eventClassDtoList);
        } else {
            eventClassDtoList = Collections.emptyList();
            callback.onSuccess(eventClassDtoList);
        }

    }

}
