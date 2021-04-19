package com.olshevchenko.currencyconverter.datasource.cache

import com.olshevchenko.currencyconverter.core.data.entity.QuoteToCurrencyRateMapper
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.datasource.local.model.EntityToLocalMapper
import com.olshevchenko.currencyconverter.datasource.local.model.LocalToEntityMapper
import com.olshevchenko.currencyconverter.datasource.local.model.RatesLocal
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.rates.domain.model.FromToCodes
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.powermock.core.classloader.annotations.PrepareForTest


@RunWith(MockitoJUnitRunner::class)
@PrepareForTest(
    LocalToEntityMapper::class,
    EntityToLocalMapper::class,
    QuoteToCurrencyRateMapper::class,
)

class RatesCacheDataSourceImplTest {

    @Mock
    lateinit var localToEntityMapperMocked: LocalToEntityMapper

    @Mock
    lateinit var entityToLocalMapperMocked: EntityToLocalMapper

    @Mock
    lateinit var quoteToCurrencyRateMapperMocked: QuoteToCurrencyRateMapper

    private val fromToCodesNulled: FromToCodes? = null
    private val fromToCodesIncorrect = FromToCodes("RU", "RU")
    private val fromToCodesMissed = FromToCodes("EUR", "EUR")
    private val fromToCodesUSDUSD = FromToCodes("USD", "USD")

    lateinit var rateUSDUSDQuote: RatesDataEntity.Quote

    lateinit var ratesEmptyLocal: RatesLocal
    lateinit var ratesUSDUSDLocal: RatesLocal

    lateinit var ratesEmptyEntity: RatesDataEntity
    lateinit var ratesUSDUSDEntity: RatesDataEntity
    lateinit var ratesEUREUREntity: RatesDataEntity
    lateinit var ratesUSDUSDEUREUREntity: RatesDataEntity

    val currencyRateUSDUSD = CurrencyRate(
        "USD", "USD",
        1.0, 12345L.toString()
    )

    lateinit var ratesCacheDataSourceImpl: RatesCacheDataSourceImpl
    lateinit var ratesCacheDataSourceImpl2: RatesCacheDataSourceImpl

    @Before
    fun setUp() {

        rateUSDUSDQuote = RatesDataEntity.Quote(12345L, 1.0)

        ratesEmptyLocal = RatesLocal(mapOf())
        ratesUSDUSDLocal = RatesLocal(sortedMapOf(Pair("USDUSD", rateUSDUSDQuote)))

        ratesEmptyEntity = RatesDataEntity(mapOf())
        ratesUSDUSDEntity = RatesDataEntity(sortedMapOf(Pair("USDUSD", rateUSDUSDQuote)))
        ratesEUREUREntity = RatesDataEntity(sortedMapOf(Pair("EUREUR", rateUSDUSDQuote)))
        ratesUSDUSDEUREUREntity = RatesDataEntity(
            sortedMapOf(
                Pair("USDUSD", rateUSDUSDQuote),
                Pair("EUREUR", rateUSDUSDQuote),
            )
        )

        `when`(localToEntityMapperMocked.map(ratesEmptyLocal))
            .thenReturn(ratesEmptyEntity)

        `when`(entityToLocalMapperMocked.map(ratesEmptyEntity))
            .thenReturn(ratesEmptyLocal)

        `when`(entityToLocalMapperMocked.map(ratesUSDUSDEntity))
            .thenReturn(RatesLocal(ratesUSDUSDEntity.quotes))

        `when`(entityToLocalMapperMocked.map(ratesEUREUREntity))
            .thenReturn(RatesLocal(ratesEUREUREntity.quotes))

        `when`(quoteToCurrencyRateMapperMocked.map(rateUSDUSDQuote, fromToCodesUSDUSD))
            .thenReturn(currencyRateUSDUSD)

        ratesCacheDataSourceImpl = RatesCacheDataSourceImpl(
            LocalToEntityMapper, entityToLocalMapperMocked, QuoteToCurrencyRateMapper
        )

        ratesCacheDataSourceImpl2 = RatesCacheDataSourceImpl(
            LocalToEntityMapper, entityToLocalMapperMocked, quoteToCurrencyRateMapperMocked
        )

    }

    @After
    fun tearDown() {
    }

    @Test
//    fun verifySaveAndGetRates() {
    fun `should get exactly the same RatesDataEntity instance back which has been saved before`() {
        ratesCacheDataSourceImpl.saveRates(ratesUSDUSDEntity)
        verify(entityToLocalMapperMocked).map(ratesUSDUSDEntity)
        assertEquals(ratesUSDUSDEntity, ratesCacheDataSourceImpl.getRates())
        assertNotEquals(ratesEUREUREntity, ratesCacheDataSourceImpl.getRates())
        assertEquals(1, ratesCacheDataSourceImpl.getRates().quotes.size)
    }

    @Test
//    fun verifyRepeatedSaveAndThenGetRates() {
    fun `should get correct rates after SEVERAL savings`() {
        ratesCacheDataSourceImpl.saveRates(ratesUSDUSDEntity)
        ratesCacheDataSourceImpl.saveRates(ratesEUREUREntity)
        assertEquals(ratesUSDUSDEUREUREntity, ratesCacheDataSourceImpl.getRates())
        assertEquals(2, ratesCacheDataSourceImpl.getRates().quotes.size)
    }

    @Test
//    fun verifySaveNullAndGetRates() {
    fun `should get correct rates after saving NULLed rates before`() {

        ratesCacheDataSourceImpl = RatesCacheDataSourceImpl(
            localToEntityMapperMocked, entityToLocalMapperMocked, quoteToCurrencyRateMapperMocked
        )

        ratesCacheDataSourceImpl.saveRates(null)
        verifyNoInteractions(entityToLocalMapperMocked)
        assertEquals(ratesEmptyEntity, ratesCacheDataSourceImpl.getRates())
        verify(localToEntityMapperMocked, times(1)).map(ratesEmptyLocal)
    }

    @Test
//    fun verifyGetCodes() {
    fun `should return list of all existed pairs of codes`() {
        ratesCacheDataSourceImpl.saveRates(ratesUSDUSDEntity)
        ratesCacheDataSourceImpl.saveRates(ratesUSDUSDEntity)
        ratesCacheDataSourceImpl.saveRates(ratesEUREUREntity)
        verify(entityToLocalMapperMocked, times(2)).map(ratesUSDUSDEntity)
        assertEquals(listOf<String>("USD", "EUR"), ratesCacheDataSourceImpl.getCodes().codes)
    }

    @Test
//    fun verifyGetEmptyCodes() {
    fun `should return empty list of codes`() {

        ratesCacheDataSourceImpl = RatesCacheDataSourceImpl(
            localToEntityMapperMocked, entityToLocalMapperMocked, quoteToCurrencyRateMapperMocked
        )

        ratesCacheDataSourceImpl.saveRates(ratesEmptyEntity)
        verify(entityToLocalMapperMocked, times(1)).map(ratesEmptyEntity)
        assertEquals(CurrencyCodes(listOf()), ratesCacheDataSourceImpl.getCodes())
    }

    @Test
//    fun verifyGetRate4NulledCodes() {
    fun `should get null result for nulled 'from-to' currency codes`() {

        ratesCacheDataSourceImpl2.saveRates(ratesUSDUSDEntity)
        assertEquals(null, ratesCacheDataSourceImpl2.getRate(fromToCodesNulled))
        verify(entityToLocalMapperMocked, times(1)).map(ratesUSDUSDEntity)
        verifyNoInteractions(quoteToCurrencyRateMapperMocked)
    }

    @Test
//    fun verifyGetRate4IncorrectCodes() {
    fun `should get null result for incorrect 'from-to' currency code`() {

        ratesCacheDataSourceImpl2.saveRates(ratesUSDUSDEntity)
        assertEquals(null, ratesCacheDataSourceImpl2.getRate(fromToCodesIncorrect))
        verify(entityToLocalMapperMocked, times(1)).map(ratesUSDUSDEntity)
        verifyNoInteractions(quoteToCurrencyRateMapperMocked)
    }

    @Test
//    fun verifyGetRate4NotFoundCodes() {
    fun `should get null result if corresponding rate is not found`() {

        ratesCacheDataSourceImpl2.saveRates(ratesUSDUSDEntity)
        assertEquals(null, ratesCacheDataSourceImpl2.getRate(fromToCodesMissed))
        verify(entityToLocalMapperMocked, times(1)).map(ratesUSDUSDEntity)
        verifyNoInteractions(quoteToCurrencyRateMapperMocked)
    }

    @Test
//    fun verifyGetRate4USDUSDCodes() {
    fun `should get correct result for correct and suitable 'from-to' currency code`() {

        ratesCacheDataSourceImpl2.saveRates(ratesUSDUSDEntity)
        assertEquals(currencyRateUSDUSD, ratesCacheDataSourceImpl2.getRate(fromToCodesUSDUSD))
        verify(entityToLocalMapperMocked, times(1)).map(ratesUSDUSDEntity)
        verify(quoteToCurrencyRateMapperMocked, times(1)).map(
            rateUSDUSDQuote, fromToCodesUSDUSD
        )
    }


}