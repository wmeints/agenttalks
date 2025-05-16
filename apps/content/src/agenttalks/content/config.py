"""Configuration for the content API."""

from pydantic_settings import BaseSettings, SettingsConfigDict


class ApplicationSettings(BaseSettings):
    """Settings for the content API.

    Attributes
    ----------
    database_url : str
        The URL of the database to connect to.
    """

    database_url: str
    model_config = SettingsConfigDict(env_file=".env", env_prefix="APP_")
