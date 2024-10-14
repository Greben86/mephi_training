# 6.9. Итоговый проект “Сервис по получению метеорологических данных”

### Что нужно делать

Необходимо реализовать сервис для вывода метеорологических данных, получив информацию с удаленного сервиса. В работе необходимо получить данные из сервиса Яндекс: https://yandex.ru/dev/weather/doc/ru/concepts/how-to

Описание API https://yandex.ru/dev/weather/doc/ru/concepts/forecast-info

### Описание реализации

- config.properties - файл с настройками
  - api.host=https://api.weather.yandex.ru
  - api.port=443
  - api.path=/v2/forecast
  - api.param.lat=lat
  - api.param.lat.value=56.298941
  - api.param.lon=lon
  - api.param.lon.value=43.945261
  - api.param.limit=limit
  - api.key.name=X-Yandex-Weather-Key
  - api.key.value=${yandex_key_value}

  Параметры api.param.lat.value и api.param.lon.value - это координаты, взял их отсюда https://www.latlong.net
  ${yandex_key_value} - вместо ключа API вот такое значение, при работе программа его распарсит и получит yandex_key_value - это название переменной окружения в которой должен быть ключ API

- Main - главный класс, запрашивает у пользователя количество дней для прогноза и запускает процесс запроса погоды
- PropertyLoaderImpl - загрузчик настроек, читает config.properties и переменные окружения
- WeatherRestClientImpl - клиент REST API, выполняет GET запрос к сервису погоды
- WeatherParserImpl - парсер ответа сервиса, читает температуру за сегодня, высчитывает среднюю температуру, форматирует JSON для удобного отображения
- WeatherPrinterImpl - принтер результатов парсинга ответа сервиса
- WeatherRecord - структура для хранения результатов парсинга 