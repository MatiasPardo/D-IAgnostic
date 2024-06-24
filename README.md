# TP - TACS - GRUPO 1

## Ejecución con Docker

1. Levantar Mongodb como base de datos:
```bash
docker run --rm --name mongo -p 27017:27017 -e MONGODB_INITDB_ROOT_USERNAME=${MONGO_USERNAME} -e MONGODB_INITDB_ROOT_PASSWORD=${MONGO_PASSWORD} mongodb/mongodb-community-server
```

2. Levantar Redis como base de datos:
```bash
docker run --rm --name redis_container -p 6379:6379 --hostname redis --network host redis:6.2-alpine
```

3. Ejecución tanto de back como de front:
	1. Ir al proyecto correspondiente
	```bash
	cd front
	# or
	cd back
	```

    2. Buildear la imagen
	```bash
	docker build -t backend .
	# or
	docker build -t frontend --build-arg REACT_APP_API_URL=${API_URL} .
	```

	3. Ejecutar el contenedor
	```bash
	docker run --rm -p 8080:8080 --name back_container -e JWT_SECRET=${JWT_SECRET} -e MONGO_USERNAME=${MONGO_USERNAME} -e MONGO_PASSWORD=${MONGO_PASSWORD} -e MONGO_HOST=${MONGO_HOST} -e MONGO_PORT=${MONGO_PORT} -e REDIS_HOST=${REDIS_HOST} -e REDIS_PORT=${REDIS_PORT} --network host backend
	# or
	docker run --rm -p 3000:80 --name front_container frontend
	```

4. Ejecutar el bot de Telegram
	1. Ir al proyecto correspondiente
	```bash
	cd telegram-bot
	```
	2. Buildear la imagen
	```bash
	docker build -t boti .
	```

	3. Ejecutar el contenedor
	```bash
	docker run --rm --name boti_container -e BOT_KEY=${BOT_KEY} -e API_URL=${API_URL} --network host boti
	```

5. (Opcional) Interface de Mongodb: Usuario y contraseña por default admin:pass
```bash
docker run --rm --name mongo_interface -p 8081:8081 -e ME_CONFIG_MONGODB_URL=mongodb://${MONGO_USERNAME}:${MONGO_PASSWORD}@${MONGO_HOST}:${MONGO_PORT} --network host mongo-express
```

La **db** expone el puerto 27017, el **back** el puerto 8080, el **front** el puerto 3000 y la **interface** de mongo el puerto 8081


## Ejecución con Docker Compose
```bash
docker-compose up
```

## Arquitectura del proyecto
El proyecto sigue una arquitectura basada en Domain Driven Design (DDD), que se organiza en cuatro capas clave: Dominio, Aplicación, Infraestructura y Presentación.

### Dominio
En la capa de Dominio, se alojan las entidades de dominio, que representan conceptos centrales en la lógica de negocio, incluyendo su estado y comportamiento. Además, esta capa es el hogar de las excepciones de dominio y, en caso de que sean necesarios, los eventos de dominio.

### Aplicación
La capa de Aplicación se encarga de orquestar los llamados a las distintas capas para resolver la ejecución de un caso de uso. Cada caso de uso se modela en una clase independiente y se invoca desde la capa de Presentación a través de los controllers. Además, la capa de Aplicación se encarga de ejecutar validaciones sobre los modelos de datos para garantizar la integridad de la información.

### Infraestructura
En la capa de Infraestructura se alojan las interfaces e implementaciones concretas de los componentes que interactúan con el mundo exterior. Esto incluye repositorios, clientes para servicios externos, servicios de terceros y otros componentes necesarios para la operación del sistema.

### Presentación
La capa de Presentación es la interfaz de usuario del sistema y recibe peticiones de los usuarios. Esta capa delega la resolución del caso de uso en la capa de Aplicación y luego devuelve los resultados al usuario. Aquí es donde se pueden utilizar mappers u otros componentes para dar formato adecuado a la salida de los datos y presentarlos de la manera apropiada.

Esta estructura de capas ayuda a mantener el código organizado, facilita la escalabilidad y permite una mayor separación de preocupaciones en el proyecto.

## Autenticación
La autenticación del sistema es stateless. Es decir, no se guarda en el servidor ningún tipo de estado para identificar a los usuarios. 
Para lograr la autenticación, se utiliza JWT. El usuario primero debe registrarse, y luego al iniciar sesión, se le entrega un token que debe incorporar en los headers (Authorization: Bearer Token) para todas las peticiones que quiera hacer

## Pruebas
Al navegar la url `localhost:8080/swagger`, se carga la pantalla de Swagger que permite interactuar con el sistema. También puede hacerse lo mismo desde Postman

## Datos de test
Para hacer pruebas, el repositorio de items es inicializado con tres items con id "abcd", "defg" y "hijk"

## Collection de postman
<details>
<summary>Mostrar JSON</summary>

```json
{
	"info": {
		"_postman_id": "e1e58114-569c-41c5-bcec-c1399d73b80c",
		"name": "tp tacs",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "16278070"
	},
	"item": [
		{
			"name": "create user",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"userName\": \"Homero\",\r\n    \"password\": \"CalleFalsa123\",\r\n    \"rol\": \"BASIC\",\r\n    \"email\": \"homeroo@gmail.comm\"\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/users",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"users"
					]
				}
			},
			"response": []
		},
		{
			"name": "login user",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"userName\": \"Homero\",\r\n    \"password\": \"CalleFalsa123\"\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/users/login",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"users",
						"login"
					]
				}
			},
			"response": []
		},
		{
			"name": "create order",
			"request": {
				"method": "POST",
				"header": [
					{
						"key": "Authorization",
						"value": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjdXJpZSIsInVzZXJJZCI6IjUyZWRlZDBjLTFhOWQtNDQ1MS1hZGNkLTIyNjJhMTcxNjEyMyIsInJvbCI6IkJBU0lDIiwiaWF0IjoxNjkzOTQ1ODE3LCJleHAiOjE2OTM5NTMwMTd9.8rv2ABLc9hAbDT6K6rhhaL2P_19D9eMQip-KPTZg6fc",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"userId\": \"2\",\r\n    \"items\": [\r\n        {\r\n            \"id\": \"abcd\",\r\n            \"quantity\": 5\r\n        }\r\n    ]\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/orders",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"orders"
					]
				}
			},
			"response": []
		},
		{
			"name": "add item to order",
			"request": {
				"method": "POST",
				"header": [
					{
						"key": "Authorization",
						"value": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjdXJpZSIsInVzZXJJZCI6IjUyZWRlZDBjLTFhOWQtNDQ1MS1hZGNkLTIyNjJhMTcxNjEyMyIsInJvbCI6IkJBU0lDIiwiaWF0IjoxNjkzOTQ1ODE3LCJleHAiOjE2OTM5NTMwMTd9.8rv2ABLc9hAbDT6K6rhhaL2P_19D9eMQip-KPTZg6fc",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"id\": \"abcd\",\r\n    \"quantity\": 5\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/orders/18d08ba1-3a87-48eb-97eb-91805d833d99/items",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"orders",
						"18d08ba1-3a87-48eb-97eb-91805d833d99",
						"items"
					]
				}
			},
			"response": []
		},
		{
			"name": "close order",
			"request": {
				"method": "PATCH",
				"header": [
					{
						"key": "Authorization",
						"value": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIb21lcm8iLCJ1c2VySWQiOiJmNDY4NjYzZS01OWE3LTQ1YjctOGQwYy0yNTdjNTg1MzRiN2MiLCJyb2wiOiJCQVNJQyIsImlhdCI6MTY5Mzk0NjM1NSwiZXhwIjoxNjkzOTUzNTU1fQ.kYThqIVvTciHYIU-cEZpDtQ9V-hzmPTlfORjG7Q4ECA",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"id\": \"abcd\",\r\n    \"quantity\": 5\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/orders/0b2dc9b8-6c02-4240-8ee5-2bfed18a25ab",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"orders",
						"0b2dc9b8-6c02-4240-8ee5-2bfed18a25ab"
					]
				}
			},
			"response": []
		},
		{
			"name": "get items from order",
			"protocolProfileBehavior": {
				"disableBodyPruning": true
			},
			"request": {
				"method": "GET",
				"header": [
					{
						"key": "Authorization",
						"value": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjdXJpZSIsInVzZXJJZCI6IjUyZWRlZDBjLTFhOWQtNDQ1MS1hZGNkLTIyNjJhMTcxNjEyMyIsInJvbCI6IkJBU0lDIiwiaWF0IjoxNjkzOTQ1ODE3LCJleHAiOjE2OTM5NTMwMTd9.8rv2ABLc9hAbDT6K6rhhaL2P_19D9eMQip-KPTZg6fc",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"id\": \"abcd\",\r\n    \"quantity\": 5\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/orders/18d08ba1-3a87-48eb-97eb-91805d833d99/items",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"orders",
						"18d08ba1-3a87-48eb-97eb-91805d833d99",
						"items"
					]
				}
			},
			"response": []
		},
		{
			"name": "add element to item",
			"request": {
				"method": "PATCH",
				"header": [
					{
						"key": "Authorization",
						"value": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjdXJpZSIsInVzZXJJZCI6ImFiYmQwNjJjLTVjNDgtNDYxMi1iZjVjLWI2NTc3YzIzMjU2MyIsInJvbCI6IkJBU0lDIiwiaWF0IjoxNjkzODgyOTU2LCJleHAiOjE2OTM4OTAxNTZ9.9S0kwUL0y3whwWdSt6saFZpv70N3bdRirprJ-XBMV6o",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "\r\n55\r\n",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/orders/3398245a-856f-460c-89eb-75c4b468653b/items/abcd",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"orders",
						"3398245a-856f-460c-89eb-75c4b468653b",
						"items",
						"abcd"
					]
				}
			},
			"response": []
		},
		{
			"name": "delete item from order",
			"request": {
				"method": "DELETE",
				"header": [
					{
						"key": "Authorization",
						"value": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIb21lcm8iLCJ1c2VySWQiOiJmNDY4NjYzZS01OWE3LTQ1YjctOGQwYy0yNTdjNTg1MzRiN2MiLCJyb2wiOiJCQVNJQyIsImlhdCI6MTY5Mzk0NjM1NSwiZXhwIjoxNjkzOTUzNTU1fQ.kYThqIVvTciHYIU-cEZpDtQ9V-hzmPTlfORjG7Q4ECA",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"quantity\": 55\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/orders/562fc81e-5407-497c-91ac-e87eea9c4982/items/abcd",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"orders",
						"562fc81e-5407-497c-91ac-e87eea9c4982",
						"items",
						"abcd"
					]
				}
			},
			"response": []
		},
		{
			"name": "get items order",
			"protocolProfileBehavior": {
				"disabledSystemHeaders": {}
			},
			"request": {
				"method": "GET",
				"header": [
					{
						"key": "Authorization",
						"value": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIb21lcm8iLCJ1c2VySWQiOiJmNDY4NjYzZS01OWE3LTQ1YjctOGQwYy0yNTdjNTg1MzRiN2MiLCJyb2wiOiJCQVNJQyIsImlhdCI6MTY5Mzk0NjM1NSwiZXhwIjoxNjkzOTUzNTU1fQ.kYThqIVvTciHYIU-cEZpDtQ9V-hzmPTlfORjG7Q4ECA",
						"type": "text"
					}
				],
				"url": {
					"raw": "http://localhost:8080/api/orders/562fc81e-5407-497c-91ac-e87eea9c4982/items",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"orders",
						"562fc81e-5407-497c-91ac-e87eea9c4982",
						"items"
					]
				}
			},
			"response": []
		},
		{
			"name": "analytics - users",
			"protocolProfileBehavior": {
				"disabledSystemHeaders": {}
			},
			"request": {
				"method": "GET",
				"header": [
					{
						"key": "Authorization",
						"value": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIb21lcm8iLCJ1c2VySWQiOiJmNDY4NjYzZS01OWE3LTQ1YjctOGQwYy0yNTdjNTg1MzRiN2MiLCJyb2wiOiJCQVNJQyIsImlhdCI6MTY5Mzk0NjM1NSwiZXhwIjoxNjkzOTUzNTU1fQ.kYThqIVvTciHYIU-cEZpDtQ9V-hzmPTlfORjG7Q4ECA",
						"type": "text"
					}
				],
				"url": {
					"raw": "http://localhost:8080/api/analytics/orders",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"analytics",
						"orders"
					]
				}
			},
			"response": []
		},
		{
			"name": "analytics - get total orders",
			"protocolProfileBehavior": {
				"disabledSystemHeaders": {}
			},
			"request": {
				"method": "GET",
				"header": [
					{
						"key": "Authorization",
						"value": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIb21lcm8iLCJ1c2VySWQiOiJmNDY4NjYzZS01OWE3LTQ1YjctOGQwYy0yNTdjNTg1MzRiN2MiLCJyb2wiOiJCQVNJQyIsImlhdCI6MTY5Mzk0NjM1NSwiZXhwIjoxNjkzOTUzNTU1fQ.kYThqIVvTciHYIU-cEZpDtQ9V-hzmPTlfORjG7Q4ECA",
						"type": "text"
					}
				],
				"url": {
					"raw": "http://localhost:8080/api/analytics/users",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"analytics",
						"users"
					]
				}
			},
			"response": []
		}
	]
}
</details>
```

