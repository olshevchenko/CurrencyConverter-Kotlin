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
#### Changed
- [core/Utils.kt] - сюда перемещён класс Utils.CurrencyCodes (из [core/data/entityRatesDataEntity.kt])
- [features/rates/domain/repository/CurrencyRatesRepository.kt] - изменён тип возврата 
CurrencyRatesRepository.getRates() с DataEntity- уровня на Domain- (Single<Result<CurrencyRates>>). 
Соотв-но, изменены: 
[core/data/RatesRepositoryImpl.kt] - метод RatesRepositoryImpl.getRates()
[features/rates/domain/usecase/GetCurrencyRatesUseCase.kt] - метод GetCurrencyRatesUseCase.buildUseCaseSingle()
#### Added
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
#### Changed
- [core/data/RatesRepositoryImpl.kt] - добавлен параметр конструктора - межслойный конвертер 
EntityToCurrencyRatesMapper. 

##2020-04-19
#### Processed
- [app/src/test/java/.../datasource/net/RatesNetworkDataSourceImplTest.kt] - дополнение Unit-тестов. 
#### Changed
- [datasource/cache/RatesCacheDataSourceImpl.kt] - упрощено хранение кэша курсов валют (теперь просто
mutableMapOf<String, RatesDataEntity.Quote>() вместо RatesLocal).
#### Milestone
- Github - публикация проекта.

##2020-04-[23-25]
#### Added
- features/rates/presentation - создание слоя Presenter для курсов валют:
[features/rates/presentation/RatesContract.kt] - контракт VP в MVP-модели
[features/rates/presentation/RatesListPresenter.kt] - презентер списка курсов валют 
#### Changed
- [core/usecase/RXUseCase.kt] - добавление функторов для обратной связи с UI при выполнении UseCase-ов
 
##2020-04-[25-26]
#### Added
- [features/rates/presentation/RatesContract.kt] - добавление view списка курсов валют
- [app/src/test/java/.../features/rates/domain/usecase/GetCurrencyRatesUseCaseTest.kt], 
[app/src/test/java/.../features/rates/presentation/RatesListPresenterTest.kt] - написание
Unit-тестов для GetCurrencyRatesUseCase, RatesListPresenter.
#### Fixed
- добавлен файл [app/src/test/resources/.../datasource/local/SavedRatesDir/.gitkeep] для занесения 
в Git тестового каталога 'SavedRatesDir' 
 
##2020-04-[27-28]
#### Added
- [features/rates/presentation/RatesListPresenter.kt] - добавление (временной?) заглушки для view. 
#### Processed
- [app/src/test/java/.../features/rates/presentation/RatesListPresenterTest.kt] - завершение 
Unit-тестов для RatesListPresenter. 
#### Added
- Github - коммит (v.0.1).

##2020-05-03
#### Changed
- [features] - переименование фич/пакетов/каталогов проекта: 
"features.rates" переименован в "features.converter" для правильного отображения бизнес-слоя. 
- [features/converter/domain/repository/CurrencyRatesRepository.kt] - изменён и расширен интерфейс:
добавлен метод refreshRates() - для обновления курсов валют, getRates() теперь просто 
возвращает существующий (на данный момент, т.е., старый) список.
- [features/converter/domain/usecase/RefreshCurrencyRatesUseCase.kt] - реализован новый UseCase
- [core/data/RatesRepositoryImpl.kt] - реализован новый refreshRates(), упрощён getRates() 
(логика обработки рез-тов сетевого запроса перенесена в refreshRates()).

##2020-05-[04-05]
#### Processed
- [app/src/test/java/.../core/data/RatesRepositoryImplTest.kt] - дополнение Unit-тестов  
RatesRepositoryImpl (@Test для refreshRates(), изменения для getRates()).

##2020-05-[06-08]
#### Processed
- [features/rates] - переименование в [features/converter]
Дальнейшее Развитие [features/converter/presentation/ConverterContract.kt] - разделение на отдельные 
контракты и группы модулей:   
[features/converter/presentation/CurrencyCodes..] ..Contract.kt, ..Presenter.kt, View.kt, ViewModel.kt, 
[features/converter/presentation/RefreshRates..] ..Contract.kt, ..Presenter.kt, View.kt, ViewModel.kt,
[features/converter/presentation/CurrencyRate..] ..Contract.kt, ..Presenter.kt, View.kt, ViewModel.kt

##2020-05-10
#### Added
- [features/rates/presentation/model] - добавление модулей описания данных рез-тов выполнения
usecase-ов доменного слоя: [CodesUI.kt], [RateUI.kt], [RefreshResultUI.kt] вместе с - ОДНОСТОРОННИМИ -
 мапперами преобразования       

##2020-05-16
#### Added
- [features/converter/domain/usecase/RefreshCurrencyRatesUseCase.kt],
[features/converter/domain/repository/CurrencyRatesRepository.kt], [core/data/RatesRepositoryImpl.kt],
[datasource/cache/RatesCacheDataSourceImpl.kt] - в возвращаемый результат обновления курсов валют  
добавлена ДАТА курсов (из новых курсов, если операция успешна, или из имеющегося кэша, в случае 
ошибка с сетью).
- [features/di/HomeModules.kt] - добавлены модули DI для presentation-слоя: презентеры и 
ConverterViewModel.
- [features/converter/presentation/ConverterFragment.kt] - добавлены Koin-инъекции презентеров и 
ConverterViewModel, добавлена работа с dataBinding - полями.  

##2020-05-17
#### Added
- [features/converter/presentation/RefreshPresenter.kt] - добавлен презентер для обновления курсов валют.
- [features/operations/presentation/OperationsContracts.kt] - реализованы наброски контракта для 2-й фичи: 
- [app/src/test/java/.../features/converter/domain/usecase/RefreshCurrencyRatesUseCaseTest.kt] - 
реализованы Unit-тесты класса RefreshCurrencyRatesUseCase.
#### Processed
- [app/src/test/java/.../core/data/RatesRepositoryImplTest.kt] - изменения Unit-тестов  
RatesRepositoryImpl для refreshRates() из-за переноса логики реализации (из refresh в getRates()). 

##2020-05-20-22
#### Added
- [res/layout/fragment_converter.xml] - продолжение р-ты с DataBinding-полями

##2020-05-[23-27]
#### Processed
- Android Studio, [build.gradle...] - возня, миграция версий, активация KAPT в проекте, ...
#### Added
- [app/src/test/java/.../features/converter/presentation/RefreshPresenterTest.kt] - написаны Unit-тесты 
класса RefreshPresenter.
- [app/src/test/java/.../datasource/cache/RatesCacheDataSourceImplTest.kt] - добавлены Unit-тесты 
проверки работы с таймштампом курсов валют.

##2020-05-[28-30]
#### Added
- [features/converter/presentation/model/RefreshResultUI.kt], [core/Utils.kt] - реализовано 
преобразование даты обновления курсов в UI-формат (Long => formatted String)  
- [features/converter/presentation/RefreshPresenter.kt] - добавлено (в parseRefreshResult()) 
преобразование даты обновления курсов из доменного формата (Long) в UI- (formatted String).
- [core/data/RatesRepositoryImpl.kt], [features/converter/domain/repository/CurrencyRatesRepository.kt],
[features/converter/domain/usecase/RefreshCurrencyRatesUseCase.kt] - тип рез-та refreshRates() 
изменён на непреобразованный, внутридоменный: <Result<Long>>

##2020-05-[30-31]
#### Added
- [features/converter/presentation/ConverterContracts.kt], 
[features/converter/presentation/ConverterFragment.kt] - добавлен контракт для View (неявный 
контракт - р-та с полями данных, ЯВНЫЙ - обновление состояний ViewModel), 
добавляется р-та с обзёрверами состояний ViewModel (обновление курсов, загрузка списка валют)
- [features/converter/presentation/ConverterViewModel.kt] - добавлены LiveData для состояний потоков
данных (domain->presenters->viewmodel->view): isRefreshingState, isCodesLoadingState, gettingRateError  
#### Removed
- [core/presentation/BaseView.kt] - убран базовый класс/интерфейс для View 

##2020-06-[05-08]
#### Added
- [res/layout/fragment_converter.xml] - упрощена логики связки DataBinding-поля isRefreshingState 
с отрисовкой спиннера "pb_refreshing" - теперь явно в разметке.

##2020-06-[08-11]
#### Changed
- [app/src/test/java/.../features/converter/presentation/ConverterViewModelTest.kt] - написание 
Unit-тестов класса ConverterViewModel. Вместо Mockito впервые используется MockK.

##2020-06-12
#### Changed
- [core/Utils.kt] => [core/UtilExt.kt]- класс полностью переделан на Kotlin extensions.

##2020-06-[17-18]
#### Changed
- [app/src/test/java/.../features/converter/presentation/RefreshPresenterTest.kt],
[app/src/test/java/.../features/converter/presentation/ConverterViewModelTest.kt] - миграция на MockK,
для проверки состояний ConverterViewModel.RefreshViewState впервые применён verifySequence.

##2020-06-[19-21]
#### Added
- [app/src/test/java/.../features/converter/di/ConverterKoinTest.kt] - написание Unit-тестов 
уровня презентации (presenter + viewModel) на базе KoinTest.
- [features/di/HomeModules.kt] - Koin-модули перегруппированы и разбиты на списки (useCase + presentation).

##2020-06-[23-30]
#### Processed
- [app/src/test/java/.../features/converter/di/ConverterKoinTest.kt] - доработка, отладка Unit-тестов 
уровня презентации на базе KoinTest.
#### Fixed
- [features/converter/presentation/RefreshPresenter.kt], [.../CodesPresenter.kt], [.../RatePresenter.kt]
 - исправлены создания обзёрверов выполнения RX-запроса (теперь они динамические для каждого 
 вызова .execute()).

##2020-06-30
#### Added
- Github - коммит (v.0.2 - Редизайн слоя Features/Presentation, доработка слоя Koin. Добавление 
DataBinding в ConverterFragment. Внедрение LiveData-полей ConverterViewModel в ConverterFragment. 
Замена Mockito на MockK в новых Unit-тестах. Добавление Unit-тестов для Converter/Refresh- фичи 
(слои ViewModel, Presenter, Koin)). 

 

############################################################################
############################################################################
############################################################################
#### ToDo

##2020-05-10
- [features/converter/presentation/ConverterPresenter.kt] - 
добавить логику быстрого возврата значений курса "1.0" для конвертации типа "USD-USD" - вообще БЕЗ 
проброса в доменный и далее слои. 

##2020-05-12
- [core/data/entity/RatesDataEntity.kt] - вынести qTimestamp из-под Quote вверх!!!

##2020-05-30
- [core/Result.kt] - добавлен тип ошибки, использовать его ВМЕСТО поля val error: Exception? !!!

##2020-06-04
Тестирование Koin + ViewModels - добавить!!!:
- [T_Ekito_koin-samples-master/samples/android-weatherapp-mvvm/app/src/test/java/org/koin/sampleapp/DryRunTest.kt]

