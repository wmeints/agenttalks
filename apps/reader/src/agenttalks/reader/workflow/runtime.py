"""Workflow runtime integration for the reader app.

This workflow runtime instance is shared across activities, the workflow implementation,
and the server.
"""

from dapr.ext.workflow import WorkflowRuntime

workflow_runtime = WorkflowRuntime()
