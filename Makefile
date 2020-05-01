rebuild:
	# force a rebuild by passing --no-cache
	docker-compose up --build --no-cache

run:
	# run as a (background) service
	docker-compose up -d

bash-app:
	# run as a service and attach to it
	docker exec -it readify-backend_application_1 sh

stop:
	# run as a service and attach to it
	docker-compose stop

remove:
	# run as a service and attach to it
	docker-compose down