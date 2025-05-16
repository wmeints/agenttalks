from dapr.ext.workflow import WorkflowRuntime

# Create a single instance of the WorkflowRuntime that can be shared across modules
# The SDK will automatically use environment variables for connection settings
workflow_runtime = WorkflowRuntime()