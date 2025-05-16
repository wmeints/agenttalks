"""GraphQL interface definition for the content API.

The GraphQL interface is defined in two parts:

- `Query`: Stored in `query.py`, this defines the schema for the data that is available
  through the API.
- `Mutation`: Stored in `mutation.py`, this defines the schema for the operations that
   can be called to mutate the data in the API.
"""

import strawberry
from strawberry.fastapi import GraphQLRouter

from agenttalks.content.graph.context import get_application_context
from agenttalks.content.graph.mutation import Mutation
from agenttalks.content.graph.query import Query

schema = strawberry.Schema(query=Query, mutation=Mutation)

router = GraphQLRouter(
    schema, prefix="/graphql", context_getter=get_application_context
)
