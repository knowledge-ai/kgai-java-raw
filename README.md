## What is this about ?

Common utilities for java microservices that work on the knowledge streaming platform


## Development & CI/CD

Project follows standard java project structure and the package manager is maven. This project uses [Spring Framework](https://spring.io/projects/spring-boot). The integration tests need a backbone services to be up and running and reachable. More instruction on how to bring up stacks soon.

- For status of the release check [github actions for this repo](https://github.com/knowledge-ai/kgai-java-raw/actions)
- For the action definition check `${REPO}/.github/workflows/${ACTION}.yaml`
- The reference directory is for reference kafka and orientdb solutions, move
 later (TODO)


[complete documentation](https://help.github.com/en/packages/using-github-packages-with-your-projects-ecosystem/configuring-apache-maven-for-use-with-github-packages)

**NOTE:** to be able to pull/push to github package you need to genrate your token with approriate permissions, to generate a token for your user you this [quick link](https://github.com/settings/tokens).  
