/*
 * Copyright (c) 2016, 2017 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.sfc.renderers.iosxe.listeners;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.genius.datastoreutils.listeners.AbstractSyncDataTreeChangeListener;
import org.opendaylight.sfc.renderers.iosxe.IosXeRspProcessor;
import org.opendaylight.yang.gen.v1.urn.cisco.params.xml.ns.yang.sfc.rsp.rev140701.RenderedServicePaths;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

@Singleton
public class RenderedPathListener extends AbstractSyncDataTreeChangeListener<RenderedServicePaths> {

    private final IosXeRspProcessor rspProcessor;

    @Inject
    public RenderedPathListener(DataBroker dataBroker, IosXeRspProcessor rspProcessor) {
        super(dataBroker, LogicalDatastoreType.CONFIGURATION, InstanceIdentifier.create(RenderedServicePaths.class));
        this.rspProcessor = rspProcessor;
    }

    @Override
    public void add(@Nonnull InstanceIdentifier<RenderedServicePaths> instanceIdentifier,
                    @Nonnull RenderedServicePaths renderedServicePaths) {
        update(instanceIdentifier, renderedServicePaths, renderedServicePaths);
    }

    @Override
    public void remove(@Nonnull InstanceIdentifier<RenderedServicePaths> instanceIdentifier,
                       @Nonnull RenderedServicePaths renderedServicePaths) {
        renderedServicePaths.getRenderedServicePath().forEach(rspProcessor::deleteRsp);
    }

    @Override
    public void update(@Nonnull InstanceIdentifier<RenderedServicePaths> instanceIdentifier,
                       @Nonnull RenderedServicePaths originalRenderedServicePaths,
                       @Nonnull RenderedServicePaths updatedRenderedServicePaths) {
        if (updatedRenderedServicePaths.getRenderedServicePath() != null) {
            updatedRenderedServicePaths.getRenderedServicePath().forEach(rspProcessor::updateRsp);
        }
    }
}
