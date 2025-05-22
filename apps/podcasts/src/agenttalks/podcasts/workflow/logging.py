"""Logging support for the workflow code."""

import logging

from dapr.ext.workflow import DaprWorkflowContext


class ReplaySafeLogger:
    """A logger that is safe to use in the workflow code."""

    def __init__(
        self, ctx: DaprWorkflowContext, base_logger: logging.Logger = None
    ) -> None:
        """
        Initialize the ReplaySafeLogger.

        Parameters
        ----------
        ctx : DaprWorkflowContext
            The context object to associate with the logger.
        base_logger : logging.Logger, optional
            An existing logger instance to use as the base logger.

            If not provided, a new logger for the current module is created.
        """
        self.ctx = ctx
        self.base_logger = base_logger or logging.getLogger(__name__)

    def _should_log(self) -> bool:
        """Check if the logger should log messages."""
        return not self.ctx.is_replaying

    def info(self, msg: str, *args: any, **kwargs: any) -> None:
        """Log an informational message if logging is enabled.

        Parameters
        ----------
        msg : str
            The message to be logged.
        *args : Any
            Additional positional arguments to pass to the logger.
        **kwargs : Any
            Additional keyword arguments to pass to the logger.
        """
        if self._should_log():
            self.base_logger.info(msg, *args, **kwargs)

    def error(self, msg: str, *args: any, **kwargs: any) -> None:
        """Log an error message if logging is enabled.

        Parameters
        ----------
        msg : str
            The message to be logged.
        *args : Any
            Additional positional arguments to pass to the logger.
        **kwargs : Any
            Additional keyword arguments to pass to the logger.
        """
        if self._should_log():
            self.base_logger.error(msg, *args, **kwargs)
