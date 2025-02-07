/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */
package org.opensearch.flowframework.transport;

import org.opensearch.action.ActionRequest;
import org.opensearch.action.ActionRequestValidationException;
import org.opensearch.common.Nullable;
import org.opensearch.core.common.io.stream.StreamInput;
import org.opensearch.core.common.io.stream.StreamOutput;
import org.opensearch.flowframework.model.Template;

import java.io.IOException;

/**
 * Transport Request to create, provision, and deprovision a workflow
 */
public class WorkflowRequest extends ActionRequest {

    /**
     * The documentId of the workflow entry within the Global Context index
     */
    @Nullable
    private String workflowId;
    /**
     * The use case template to index
     */
    @Nullable
    private Template template;
    /**
     * Validation flag
     */
    private String[] validation;

    /**
     * Provision flag
     */
    private boolean provision;

    /**
     * Instantiates a new WorkflowRequest, set validation to false and set requestTimeout and maxWorkflows to null
     * @param workflowId the documentId of the workflow
     * @param template the use case template which describes the workflow
     */
    public WorkflowRequest(@Nullable String workflowId, @Nullable Template template) {
        this(workflowId, template, new String[] { "all" }, false);
    }

    /**
     * Instantiates a new WorkflowRequest
     * @param workflowId the documentId of the workflow
     * @param template the use case template which describes the workflow
     * @param validation flag to indicate if validation is necessary
     * @param provision flag to indicate if provision is necessary
     */
    public WorkflowRequest(@Nullable String workflowId, @Nullable Template template, String[] validation, boolean provision) {
        this.workflowId = workflowId;
        this.template = template;
        this.validation = validation;
        this.provision = provision;
    }

    /**
     * Instantiates a new Workflow request
     * @param in The input stream to read from
     * @throws IOException If the stream cannot be read properly
     */
    public WorkflowRequest(StreamInput in) throws IOException {
        super(in);
        this.workflowId = in.readOptionalString();
        String templateJson = in.readOptionalString();
        this.template = templateJson == null ? null : Template.parse(templateJson);
        this.validation = in.readStringArray();
        this.provision = in.readBoolean();
    }

    /**
     * Gets the workflow Id of the request
     * @return the workflow Id
     */
    @Nullable
    public String getWorkflowId() {
        return this.workflowId;
    }

    /**
     * Gets the use case template of the request
     * @return the use case template
     */
    @Nullable
    public Template getTemplate() {
        return this.template;
    }

    /**
     * Gets the validation flag
     * @return the validation boolean
     */
    public String[] getValidation() {
        return this.validation;
    }

    /**
     * Gets the provision flag
     * @return the provision boolean
     */
    public boolean isProvision() {
        return this.provision;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeOptionalString(workflowId);
        out.writeOptionalString(template == null ? null : template.toJson());
        out.writeStringArray(validation);
        out.writeBoolean(provision);
    }

    @Override
    public ActionRequestValidationException validate() {
        return null;
    }
}
