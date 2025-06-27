# Non-functional requirements

This section covers how we approach non-functional requirements.

## Robustness

We're relying heavily on AI and we know from experience that it can be unavailable.
This happens more often than we like, so we'll need to introduce the necessary robustness
measures into the software.

## Security

We're dealing with public information so we're not worried about people looking at our
stuff. In fact, we want people to look at the data that we gathered and listen to the podcast.

However, it is important that we control what content is submitted to the podcast. If we
were to publish trash on the podcast our reputations are quickly ruined. So we're limiting
access to the podcast generator by way of authentication through our internal authentication server at Info Support.

## Maintainability

We don't have a lot of time to maintain this solution. So we prefer simple techniques
over complex ones. And we love open-source because it means more people get to fix the
issues in our code and frameworks that we use.
