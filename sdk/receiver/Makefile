TOP:=$(PWD)
DISTRIBUTE_PATH:=$(TOP)/dist
EXAMPLE_PATH:=$(TOP)/src/examples
VERSION:=1.0
SDK_NAME:=joycast.receiver.sdk
MODULE_NAME:=joycast.receiver

# You can edit Webserver Information to deploy examples
WEBSERVER_ROOT:=/var/www/html/thkang2/
WEBSERVER_IP:=10.0.12.157
WEBSERVER_ID:=root
########################################################

all: premake build

.phony:premake
premake:
	@echo 'Making distribution directories...'
	@mkdir -p $(DISTRIBUTE_PATH)
	@mkdir -p $(DISTRIBUTE_PATH)/doc/internal
	@mkdir -p $(DISTRIBUTE_PATH)/doc/external
	@cp -fr $(EXAMPLE_PATH) $(DISTRIBUTE_PATH)/

.phony:build
build: premake
	@echo 'Compile typescript sources...'
	@tsc -p tsconfig.json
	@echo 'Distributing...'
	@browserify $(DISTRIBUTE_PATH)/receiver/$(SDK_NAME).js \
			-o $(DISTRIBUTE_PATH)/examples/js/$(SDK_NAME).js --standalone $(MODULE_NAME)
	@babel $(DISTRIBUTE_PATH)/examples/js/$(SDK_NAME).js \
			--out-file $(DISTRIBUTE_PATH)/examples/js/$(SDK_NAME).js \
			--presets=es2015 --compact --no-babelrc
	@cp $(DISTRIBUTE_PATH)/examples/js/$(SDK_NAME).js $(EXAMPLE_PATH)/js/

.phony:release
release: build
	@echo 'Releasing and Minifying...'
	@babel $(DISTRIBUTE_PATH)/examples/js/$(SDK_NAME).js \
			--out-file $(DISTRIBUTE_PATH)/examples/js/$(SDK_NAME)-$(VERSION).min.js --presets=babili --no-babelrc
	@echo 'Generating API Documents for internal'
	@node_modules/.bin/typedoc --options typedoc-internal.json src/receiver/$(SDK_NAME).ts
	@echo 'Generating API Documents for external'
	@node_modules/.bin/typedoc --options typedoc-external.json src/receiver/$(SDK_NAME).ts

.phony:deploy
deploy: release
	@echo 'Deploying to your webserver...'
	@echo 'You may need root password on your system'
	@sudo rsync -avz $(DISTRIBUTE_PATH) $(WEBSERVER_ID)@$(WEBSERVER_IP):$(WEBSERVER_ROOT)/$(SDK_NAME)

clean:
	@rm -fr $(DISTRIBUTE_PATH)