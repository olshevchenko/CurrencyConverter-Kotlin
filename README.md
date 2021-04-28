CurrencyConverter-Kotlin - an Android Native Application
========================================================

This is a test App on Kotlin for interacting with currency rates service "currencylayer API" using the following features of Android development:

* Clean architecture
* Dependency Injection w. Koin 
* Repository pattern
* MVP Android Architecture
* Async w. RxJava
* Architecture Components (ViewModel, LiveData, Navigation, ..)
* Unit testing w. Mockito

ToDo
----
Приложение использует данные из http://www.currencylayer.com

Необходимо создать конвертор валют из двух экранов:
1) Экран конвертации валюты
 - Выбор валюты – два ниспадающих списка (from и to). Если пользователь уже
осуществлял вход на данном устройстве, то отображать значения валют из
прошлого сеанса работы;
 - Кнопка получения курса валют (в процессе запроса должен показываться прогресс-
бар);
 - В случае, если пользователь не подключен к интернету, то показывать последнее
сохраненное значение курса из БД, если такого курса нет в БД, показать сообщение
об этом;
2) История запросов
 - Список с историей операций пользователя (валюта from – валюта to – курс – дата);
 - По свайпу влево по элементу истории должна появляться кнопка delete,
удаляющая элемент истории.
Экран конвертации валюты и истории организовать в виде табов.

Introduction
------------

2020-02-05
Currencylayer API:
!!! ВНИМАНИЕ !!! Существующее ограничение на кол-во запросов к серверу (на тарифном плане FREE) = 
250 запросов/мес. 
Ответ на запрос к [Currencylayer API](http://apilayer.net/api/live?access_key=...&currencies=EUR,GBP,CAD,RUB&source=USD&format=1):
```json
{
  "success":true,
  "terms":"https://currencylayer.com/terms",
  "privacy":"https://currencylayer.com/privacy",
  "timestamp":1612577526,
  "source":"USD",
  "quotes":{
    "USDEUR":0.829941,
    "USDGBP":0.727987,
    "USDCAD":1.275115,
    "USDRUB":74.649404
  }
}
```

2020-02-18
Fixer API:
!!! ВНИМАНИЕ !!! Существующее ограничение на кол-во запросов к серверу (на тарифном плане FREE) = 
1000 запросов/мес. 
Ответ на запрос к [Fixer API](https://data.fixer.io/api/latest?access_key=API_KEY):
```json
{
    "success": true,
    "timestamp": 1519296206,
    "base": "EUR",
    "date": "2021-02-18",
    "rates": {
        "AUD": 1.566015,
        "CAD": 1.560132,
        "CHF": 1.154727,
        "CNY": 7.827874,
        "GBP": 0.882047,
        "JPY": 132.360679,
        "USD": 1.23396
    }
} 
```

2020-03-06
Currencylayer API:
ПОЛНЫЙ список валют - ответ на [запрос](http://apilayer.net/api/live?access_key=...&format=1): 
```json
{
  "success":true,
  "terms":"https:\/\/currencylayer.com\/terms",
  "privacy":"https:\/\/currencylayer.com\/privacy",
  "timestamp":1614910386,
  "source":"USD",
  "quotes":{
    "USDAED":3.673201,
    "USDAFN":78.827906,
    "USDALL":103.372459,
    "USDAMD":524.979709,
    "USDANG":1.805946,
    "USDAOA":626.413973,
    "USDARS":90.302597,
    "USDAUD":1.29968,
    "USDAWG":1.8,
    "USDAZN":1.699197,
    "USDBAM":1.635091,
    "USDBBD":2.031417,
    "USDBDT":85.312498,
    "USDBGN":1.633809,
    "USDBHD":0.376977,
    "USDBIF":1957.196004,
    "USDBMD":1,
    "USDBND":1.339882,
    "USDBOB":6.947089,
    "USDBRL":5.6694,
    "USDBSD":1.006174,
    "USDBTC":2.1415085e-5,
    "USDBTN":73.234657,
    "USDBWP":11.08656,
    "USDBYN":2.625819,
    "USDBYR":19600,
    "USDBZD":2.02799,
    "USDCAD":1.269135,
    "USDCDF":1995.000292,
    "USDCHF":0.92926,
    "USDCLF":0.026532,
    "USDCLP":732.111164,
    "USDCNY":6.475899,
    "USDCOP":3644.9,
    "USDCRC":616.17175,
    "USDCUC":1,
    "USDCUP":26.5,
    "USDCVE":92.180172,
    "USDCZK":21.967703,
    "USDDJF":179.121573,
    "USDDKK":6.220497,
    "USDDOP":58.162788,
    "USDDZD":133.453195,
    "USDEGP":15.700201,
    "USDERN":14.99979,
    "USDETB":40.689707,
    "USDEUR":0.836545,
    "USDFJD":2.03385,
    "USDFKP":0.72076,
    "USDGBP":0.720765,
    "USDGEL":3.325017,
    "USDGGP":0.72076,
    "USDGHS":5.7649,
    "USDGIP":0.72076,
    "USDGMD":51.349765,
    "USDGNF":10198.470092,
    "USDGTQ":7.73686,
    "USDGYD":210.488476,
    "USDHKD":7.76112,
    "USDHNL":24.24592,
    "USDHRK":6.344301,
    "USDHTG":77.51787,
    "USDHUF":305.464967,
    "USDIDR":14368.55,
    "USDILS":3.30901,
    "USDIMP":0.72076,
    "USDINR":73.144978,
    "USDIQD":1467.905631,
    "USDIRR":42105.000412,
    "USDISK":128.070095,
    "USDJEP":0.72076,
    "USDJMD":151.091827,
    "USDJOD":0.709023,
    "USDJPY":107.910825,
    "USDKES":110.320214,
    "USDKGS":84.788904,
    "USDKHR":4098.90064,
    "USDKMF":411.596907,
    "USDKPW":899.970646,
    "USDKRW":1132.044975,
    "USDKWD":0.30265,
    "USDKYD":0.838349,
    "USDKZT":422.80297,
    "USDLAK":9413.12199,
    "USDLBP":1521.586463,
    "USDLKR":196.94464,
    "USDLRD":173.89681,
    "USDLSL":15.289783,
    "USDLTL":2.95274,
    "USDLVL":0.60489,
    "USDLYD":4.501741,
    "USDMAD":9.004824,
    "USDMDL":17.707414,
    "USDMGA":3755.89386,
    "USDMKD":51.510667,
    "USDMMK":1418.658574,
    "USDMNT":2848.296896,
    "USDMOP":8.039644,
    "USDMRO":356.999828,
    "USDMUR":39.846468,
    "USDMVR":15.409783,
    "USDMWK":786.077949,
    "USDMXN":21.211005,
    "USDMYR":4.068499,
    "USDMZN":74.564974,
    "USDNAD":15.289987,
    "USDNGN":379.999706,
    "USDNIO":35.113489,
    "USDNOK":8.617795,
    "USDNPR":117.176632,
    "USDNZD":1.396515,
    "USDOMR":0.384996,
    "USDPAB":1.006044,
    "USDPEN":3.69591,
    "USDPGK":3.532667,
    "USDPHP":48.645501,
    "USDPKR":158.134196,
    "USDPLN":3.817495,
    "USDPYG":6716.745463,
    "USDQAR":3.641023,
    "USDRON":4.081899,
    "USDRSD":98.239366,
    "USDRUB":74.637499,
    "USDRWF":999.506755,
    "USDSAR":3.751259,
    "USDSBD":8.019128,
    "USDSCR":21.203604,
    "USDSDG":379.498186,
    "USDSEK":8.524901,
    "USDSGD":1.338794,
    "USDSHP":0.72076,
    "USDSLL":10203.000203,
    "USDSOS":582.999866,
    "USDSRD":14.153974,
    "USDSTD":20282.133983,
    "USDSVC":8.80317,
    "USDSYP":512.775718,
    "USDSZL":15.115704,
    "USDTHB":30.463008,
    "USDTJS":11.464424,
    "USDTMT":3.5,
    "USDTND":2.751497,
    "USDTOP":2.277749,
    "USDTRY":7.526698,
    "USDTTD":6.826344,
    "USDTWD":27.8915,
    "USDTZS":2319.000297,
    "USDUAH":27.901702,
    "USDUGX":3679.326517,
    "USDUSD":1,
    "USDUYU":43.610827,
    "USDUZS":10511.620519,
    "USDVEF":9.9875,
    "USDVND":23027.5,
    "USDVUV":107.76832,
    "USDWST":2.510208,
    "USDXAF":548.385667,
    "USDXAG":0.039771,
    "USDXAU":0.000592,
    "USDXCD":2.70255,
    "USDXDR":0.69999,
    "USDXOF":548.406299,
    "USDXPF":100.150251,
    "USDYER":250.400744,
    "USDZAR":15.329798,
    "USDZMK":9001.200141,
    "USDZMW":22.053739,
    "USDZWL":322.000331
  }
}
```