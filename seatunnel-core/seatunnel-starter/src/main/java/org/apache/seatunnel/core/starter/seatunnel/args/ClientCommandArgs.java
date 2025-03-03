/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.core.starter.seatunnel.args;

import org.apache.seatunnel.common.config.Common;
import org.apache.seatunnel.common.config.DeployMode;
import org.apache.seatunnel.core.starter.command.AbstractCommandArgs;
import org.apache.seatunnel.core.starter.command.Command;
import org.apache.seatunnel.core.starter.enums.MasterType;
import org.apache.seatunnel.core.starter.seatunnel.command.ClientExecuteCommand;
import org.apache.seatunnel.core.starter.seatunnel.command.SeaTunnelConfValidateCommand;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class ClientCommandArgs extends AbstractCommandArgs {
    @Parameter(names = {"-m", "--master"},
        description = "SeaTunnel job submit master, support [client, cluster]",
        converter = SeaTunnelMasterTargetConverter.class)
    private MasterType masterType = MasterType.LOCAL;

    @Parameter(names = {"-cn", "--cluster"},
        description = "The name of cluster")
    private String clusterName = "seatunnel_default_cluster";

    @Parameter(names = {"-j", "--job-id"},
        description = "Get job status by JobId")
    private String jobId;

    @Parameter(names = {"-can", "--cancel-job"},
        description = "Cancel job by JobId")
    private String cancelJobId;

    @Parameter(names = {"--metrics"},
        description = "Get job metrics by JobId")
    private String metricsJobId;

    @Parameter(names = {"-l", "--list"},
        description = "list job status")
    private boolean listJob = false;

    @Override
    public Command<?> buildCommand() {
        Common.setDeployMode(getDeployMode());
        if (checkConfig) {
            return new SeaTunnelConfValidateCommand(this);
        } else {
            return new ClientExecuteCommand(this);
        }
    }

    public MasterType getMasterType() {
        return masterType;
    }

    public void setMasterType(MasterType masterType) {
        this.masterType = masterType;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCancelJobId() {
        return cancelJobId;
    }

    public void setCancelJobId(String cancelJobId) {
        this.cancelJobId = cancelJobId;
    }

    public String getMetricsJobId() {
        return metricsJobId;
    }

    public void setMetricsJobId(String metricsJobId) {
        this.metricsJobId = metricsJobId;
    }

    public boolean isListJob() {
        return listJob;
    }

    public void setListJob(boolean listJob) {
        this.listJob = listJob;
    }

    public DeployMode getDeployMode() {
        return DeployMode.CLIENT;
    }

    public static class SeaTunnelMasterTargetConverter implements IStringConverter<MasterType> {
        private static final List<MasterType> MASTER_TYPE_LIST = new ArrayList<>();

        static {
            MASTER_TYPE_LIST.add(MasterType.LOCAL);
            MASTER_TYPE_LIST.add(MasterType.CLUSTER);
        }

        @Override
        public MasterType convert(String value) {
            MasterType masterType = MasterType.valueOf(value.toUpperCase());
            if (MASTER_TYPE_LIST.contains(masterType)) {
                return masterType;
            } else {
                throw new IllegalArgumentException("SeaTunnel job on st-engine submitted target only " +
                        "support these options: [local, cluster]");
            }
        }
    }
}
