"""Shared types and utilities for the application."""

import json
from datetime import datetime


class ApplicationJSONEncoder(json.JSONEncoder):
    """Custom JSON encoder for the application."""

    def default(self, obj: any):
        """Provide a sensible default while encoding values.

        Parameters
        ----------
        obj : any
            The object to encode.

        Returns
        -------
        any
            The encoded object.
        """
        if isinstance(obj, datetime):
            return obj.isoformat()

        return super().default(obj)
