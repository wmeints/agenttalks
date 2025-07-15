# Merge Backend APIs

- Merge all the backend APIs into the content-api application.  Make sure to use feature slicing to merge the different features represented in the APIs.
- Merge the database migrations into one new migration containing the database structures of the content-api and all the other APIs.
- Merge the maven dependencies making sure that the merged code still works in the content-api.
- Directly call into the features instead of using client proxies to communicate between different the different feature slices.
- Use the vert.x eventbus instead of RabbitMQ when exchanging events.

