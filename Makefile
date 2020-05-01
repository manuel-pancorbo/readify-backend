rebuild:
	# force a rebuild by passing --no-cache
	docker-compose -f up docker-compose-prod.yml --build --no-cache

run:
	# run as a (background) service
	docker-compose -f up docker-compose-prod.yml -d

bash-app:
	# run as a service and attach to it
	docker exec -it readify-backend_application_1 sh

stop:
	# run as a service and attach to it
	docker-compose -f docker-compose-prod.yml stop

remove:
	# run as a service and attach to it
	docker-compose -f docker-compose-prod.yml down