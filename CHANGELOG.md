# Change Log of the AndroidCurrencyConverter project
All notable changes to this project will be documented in this file.

Here are **types of changes** used in the file:  
- #### Milestone
- #### Processed
- #### Added
- #### Changed
- #### Fixed
- #### Removed
- #### ToDo


##2020-02-05
#### Milestone
- Начало создания проекта


##2020-02-[13-14]
#### Added
- Features


##2020-02-[15-17]
#### Added
- features/domain/usecase слой
- начало написания core/usecase - базы ([core/usecase/UseCase.kt])


##2020-02-18
#### Added
- [versions.gradle] - упорядочена р-та с версиями библиотек
- начало написания datasource/net - базы


##2020-02-19
#### Processed
- datasource/net/model - написание модели RateApi
- datasource/net/api - написание RatesApiService


##2020-02-[20-22]
#### Added
- [core/Result.kt] - добавление универсальной обёртки "результата" операции
- Принятие решения по иерархии слоёв:
  1) репозитории на ур. домена + данных: core/data/
     RatesRepositoryImpl <- CurrencyRatesRepository  
  2) источник данных: core/data/datasource/
     RatesNetworkDataSourceImpl <- RatesDataSource
- Принятие решения по межслойному маппингу данных (согласно DI - на "внешних", нижних слоях знаем про 
внутренние/верхнего уровня и конвертим данные там, внизу)
- [core/BaseMapper.kt] - добавление универсального интерфейса - маппера данных

     
##2020-02-[23-24]
#### Added
- [datasource/net/model/RateApi.kt] - добавление ApiToEntityMapper - конвертера списка курсов для 
уровня RatesDataEntity
- [datasource/net/exceptions/HandleNetworkException.kt] - добавление разборщика сетевых ошибок-исключений
- [RatesNetworkDataSourceImpl.kt] - разбор ответа от ratesApiService с разделением на успешный (со 
списком курсов) и не успешный (с кодом и пояснением ошибки)

     
##2020-02-[23-26]
#### Added
- [datasource/di/DataModules.kt] - добавление модуля DI с компонентами источников данных  
- datasource/local/model - создание [RatesLocal.kt] - модели RatesLocal + конвертеров 
(LocalToEntity, EntityToLocal) 
#### Removed
- [datasource/RatesDataSource.kt] - убран базовый интерфейс RatesDataSource (как необеспечивающий 
универсальности контракта различных реализаций Rates___DataSourceImpl)


##2020-02-28
#### Added
- datasource/cache - создание [RatesCacheDataSourceImpl.kt] - данные о курсах валют, хранящиеся в памяти 
- datasource/local - создание [RatesLocalDataSourceImpl.kt] - данные о курсах валют, хранящиеся в 
файловой системе


##2020-03-01
#### Processed
- [datasource/local/RatesLocalDataSourceImpl.kt] - продолжение работы
#### Added
- features/di - создание [HomeModule.kt] - модуля DI с компонентами USeCase, ...  


##2020-03-02
#### Processed
- features/rates/domain/usecase - изменения [____CurrencyRatesUseCase.kt] в списках и реализации 
UseCase (добавлены GetRates, SaveRates, GetCurrencies, GetRate).
Соотв-но, изменён [features/rates/domain/repository/CurrencyRatesRepository.kt]
#### Added
- [features/rates/domain/model/CurrencyCodes.kt] - добавлен список кодов валют   
features/di - создание [HomeModule.kt] - модуля DI с компонентами UseCase, ...  
 

##2020-03-[04-10]
#### Added
- rates/domain/usecase/ - добавление перечня сценариев (GetCurrencyCodesUseCase, ..) 
- [datasource/net/model/RatesApi.kt] - написание CurrencyCodeUtils для конверсии 6-значных 
 кодов валючных пар в 3-значные коды валют и обратно  
#### Processed
- [features/rates/domain/repository/CurrencyRatesRepository.kt] - доработка методов интерфейса
- [core/data/RatesRepositoryImpl.kt] - доработка getRates(), saveRates(), реализация getCodes() и 
getRate()

 
##2020-03-[06-19]
#### Added
- [app/src/test/java/.../core/data/RatesRepositoryImplTest.kt] - написание Unit-тестов класса 
RatesRepositoryImpl
- [app/src/test/java/.../datasource/cache/RatesCacheDataSourceImplTest.kt] - написание Unit-тесов класса 
RatesCacheDataSourceImpl
#### Processed
- [features/di/HomeModule.kt] - добавлены потоки получения и обработки UseCase-ов, ...


##2020-03-[20-22]
#### Added
- [*.gradle.kts], [buildSrc/.../Build*] - новые скрипты сборки проекта (замена Gradle Groovy на 
Kotlin DSL plugin в сборке)

 
##2020-03-[24-28]
#### Added
- [app/src/test/java/.../datasource/local/RatesLocalDataSourceImplTest.kt] - написание Unit-тестов класса 
RatesLocalDataSourceImpl, возня с файловыми операциями (запись/чтение файла с JSON-представлением 
списка курсов валют), изучение способов моккирования вывода в лог. 

 
##2020-04-03
#### Processed
- [core/Result.kt] - расширение класса новым значением рез-та "ошибка + данные" 

 
##2020-04-[04-06]
#### Added
- [app/src/test/java/.../features/rates/domain/usecase/GetCurrencyRateUseCaseTest.kt] - написание 
Unit-тестов класса GetCurrencyRateUseCase. Замена PowerMockRunner на MockitoJUnitRunner, отладка 
тестовых сценариев.
#### Processed
- [datasource/cache/RatesCacheDataSourceImpl.kt] - исправление логики обработки ошибок в getCodes().
#### Added
- [app/src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker] - добавление флага разрешения
моккирования final-классов.


##2020-04-[07-09]
#### Added
- [app/src/test/java/.../features/rates/domain/usecase/GetCurrencyCodesUseCaseTest.kt] - написание 
Unit-тестов класса GetCurrencyCodesUseCase.
#### Processed
- [app/src/test/java/.../core/data/RatesRepositoryImplTest.kt] - дополнение Unit-тестов  
RatesRepositoryImpl (@Test для getRates()).
- [app/src/test/java/.../datasource/cache/RatesCacheDataSourceImplTest.kt] - дополнение Unit-тесов 
класса RatesCacheDataSourceImpl (слияние старого кэша с новыми курсами).
- [core/data/RatesRepositoryImpl.kt] - getRates() теперь возвращает актуальный объединённый список
 курсов валют.


##2020-04-14
- #### Changed
- [core/Utils.kt] - сюда перемещён класс Utils.CurrencyCodes (из [core/data/entityRatesDataEntity.kt])
- [features/rates/domain/repository/CurrencyRatesRepository.kt] - изменён тип возврата 
CurrencyRatesRepository.getRates() с DataEntity- уровня на Domain- (Single<Result<CurrencyRates>>). 
Соотв-но, изменены: 
[core/data/RatesRepositoryImpl.kt] - метод RatesRepositoryImpl.getRates()
[features/rates/domain/usecase/GetCurrencyRatesUseCase.kt] - метод GetCurrencyRatesUseCase.buildUseCaseSingle()
- #### Added
- [datasource/cache/RatesCacheDataSourceImpl.kt] - маппер QuoteToCurrencyRateMapper добавлен 
в параметры конструктора класса.
#### Processed
- [app/src/test/java/.../datasource/cache/RatesCacheDataSourceImplTest.kt] - дополнение Unit-тестов 
класса RatesCacheDataSourceImpl - разл. варианты getRate().


##2020-04-[15-18]
#### Added
- datasource/net/api/ - отладка сетевого взаимодействия: 
- [datasource/net/api/MockInterceptor.kt] - добавлен OkHttp мок-интерцептор для подмены ответов сервера 
- [app/src/test/java/.../datasource/net/RatesNetworkDataSourceImplTest.kt] - написание Unit-тестов класса 
RatesNetworkDataSourceImpl, добавление расширенной функции для сравнения Result<RatesDataEntity>.error.
Добавлены разл. тесты RatesNetworkDataSourceImpl.getRates() с использованием MockInterceptor - 
с возвратом ошибки и коротких/длинных ответов. 


##2020-04-18
- #### Changed
- [core/data/RatesRepositoryImpl.kt] - добавлен параметр конструктора - межслойный конвертер 
EntityToCurrencyRatesMapper. 

##2020-04-19
#### Processed
- [app/src/test/java/.../datasource/net/RatesNetworkDataSourceImplTest.kt] - дополнение Unit-тестов 
- #### Changed
- [datasource/cache/RatesCacheDataSourceImpl.kt] - упрощено хранение кэша курсов валют (теперь просто
mutableMapOf<String, RatesDataEntity.Quote>() вместо RatesLocal)

