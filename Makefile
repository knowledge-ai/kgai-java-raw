help: ## Lists available commands and their explanation
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

build-docker: ## builds docker image based on release scripts
	. ./release_util.sh

build-release: ## release a new version of the app and build the docker for it
	export APP_RELEASE=1 && . ./release_util.sh && unset APP_RELEASE

run-app: ## runs the dockerized app
	docker run knowledgeai/kgai-java-raw:$(shell cat "./version.md")

run-app-interactive: ## runs the dockerized app interactive mode
	docker run -it knowledgeai/kgai-java-raw:$(shell cat "./version.md")

run-tests: ## runs tests for the app through maven
	mvn test
