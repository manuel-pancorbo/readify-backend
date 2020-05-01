rebuild:
	# force a rebuild by passing --no-cache
	docker-compose -f docker-compose-prod.yml up --build --no-cache

run:
	# run as a (background) service
	docker-compose -f docker-compose-prod.yml up -d

bash-app:
	# run as a service and attach to it
	docker exec -it readify-backend_application_1 sh
JpaPaymentRepositoryShould
stop:
	# run as a service and attach to it
	docker-compose -f docker-compose-prod.yml stop

remove:
	# run as a service and attach to it
	docker-compose -f docker-compose-prod.yml down