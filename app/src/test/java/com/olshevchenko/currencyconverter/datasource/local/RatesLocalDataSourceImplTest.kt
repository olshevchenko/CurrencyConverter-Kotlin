package com.olshevchenko.currencyconverter.datasource.local

import android.util.Log
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.datasource.local.model.EntityToLocalMapper
import com.olshevchenko.currencyconverter.datasource.local.model.LocalToEntityMapper
import com.olshevchenko.currencyconverter.datasource.local.model.RatesLocal
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.MockitoJUnitRunner
import org.powermock.core.classloader.annotations.PrepareForTest
import java.io.File

@RunWith(MockitoJUnitRunner::class)
@PrepareForTest(
    LocalToEntityMapper::class,
    EntityToLocalMapper::class,
    File::class,
    Log::class
)
class RatesLocalDataSourceImplTest {

    lateinit var ratesLocalDataSourceImpl: RatesLocalDataSourceImpl

    lateinit var file: File
    lateinit var fileInvalid: File

    @Mock
    lateinit var localToEntityMapperMocked: LocalToEntityMapper

    @Mock
    lateinit var entityToLocalMapperMocked: EntityToLocalMapper

    lateinit var rateQuote1: RatesDataEntity.Quote

    lateinit var ratesUSDUSDEntity: RatesDataEntity
    lateinit var ratesEUREUREntity: RatesDataEntity
    lateinit var ratesEntities2Save3Pairs: RatesDataEntity
    lateinit var ratesEmptyEntity: RatesDataEntity

    lateinit var ratesUSDUSDLocal: RatesLocal
    lateinit var ratesEUREURLocal: RatesLocal
    lateinit var ratesLocals: RatesLocal

    @Before
    fun setUp() {

        rateQuote1 = RatesDataEntity.Quote(12345L, 1.0)

        ratesUSDUSDEntity = RatesDataEntity(sortedMapOf(Pair("USDUSD", rateQuote1)))
        ratesEUREUREntity = RatesDataEntity(sortedMapOf(Pair("EUREUR", rateQuote1)))

        ratesEntities2Save3Pairs = RatesDataEntity(
            sortedMapOf(
                Pair("USDUSD", RatesDataEntity.Quote(12345L, 1.0)),
                Pair("USDEUR", RatesDataEntity.Quote(12345L, 0.8)),
                Pair("USDRUB", RatesDataEntity.Quote(12345L, 75.0)),
            )
        )

        ratesEmptyEntity = RatesDataEntity(sortedMapOf())

        ratesUSDUSDLocal = RatesLocal(sortedMapOf(Pair("USDUSD", rateQuote1)))
        ratesEUREURLocal = RatesLocal(sortedMapOf(Pair("EUREUR", rateQuote1)))

        ratesLocals = RatesLocal(
            sortedMapOf(
                Pair("USDUSD", RatesDataEntity.Quote(12345L, 1.0)),
                Pair("USDEUR", RatesDataEntity.Quote(12345L, 0.8)),
                Pair("USDRUB", RatesDataEntity.Quote(12345L, 75.0)),
            )
        )

        file = File(
            (RatesLocalDataSourceImplTest::class.java.getResource("SavedRates.txt"))!!.path
        )

        fileInvalid = File(
            (RatesLocalDataSourceImplTest::class.java.getResource("SavedRatesDir"))!!.path
        )

//        `when`(entityToLocalMapperMocked.map(ratesUSDUSDEntity))
//            .thenReturn(ratesUSDUSDLocal)
//
//        `when`(entityToLocalMapperMocked.map(ratesEUREUREntity))
//            .thenReturn(ratesEUREURLocal)
//
//        `when`(localToEntityMapperMocked.map(ratesUSDUSDLocal))
//            .thenReturn(ratesUSDUSDEntity)
//
//        `when`(localToEntityMapperMocked.map(ratesEUREURLocal))
//            .thenReturn(ratesEUREUREntity)

//        ratesLocalDataSourceImpl = RatesLocalDataSourceImpl(
//            file, localToEntityMapperMocked, entityToLocalMapperMocked
        ratesLocalDataSourceImpl = RatesLocalDataSourceImpl(
            file, LocalToEntityMapper, EntityToLocalMapper
        )
    }

    @After
    fun tearDown() {
    }

    @Test
//    fun verifySaveRates() {
    fun `should successfully save RatesDataEntity into the file`() {
        var result = ratesLocalDataSourceImpl.saveRates(ratesEntities2Save3Pairs)
        `verifyNoInteractions`(entityToLocalMapperMocked)
        assertTrue(result)
    }

    @Test
//    fun verifyLoadRates() {
    fun `should successfully load RatesDataEntity previously saved into the file`() {
        var ratesEntities2Load = ratesLocalDataSourceImpl.loadRates()
        assertEquals(ratesEntities2Save3Pairs, ratesEntities2Load)
    }


    @Test
//    fun verifySaveAndThenLoadEmptyRates() {
    fun `should successfully load EMPTY RatesEntity previously saved into the file`() {
        var result = ratesLocalDataSourceImpl.saveRates(ratesEntities2Save3Pairs)
        assertTrue(result)
        result = ratesLocalDataSourceImpl.saveRates(ratesEmptyEntity)
        assertTrue(result)
        var ratesEntitiesLoaded = ratesLocalDataSourceImpl.loadRates()
        assertNotEquals(ratesEntities2Save3Pairs, ratesEntitiesLoaded)
        assertEquals(ratesEmptyEntity, ratesEntitiesLoaded)
    }

    @Test
//    fun verifySaveRates2NonFile() {
    fun `should return error after try to save RatesDataEntity into non-file`() {
        var ratesLocalDataSourceNonFileImpl = RatesLocalDataSourceImpl(
            fileInvalid, LocalToEntityMapper, EntityToLocalMapper
        )
        var result = ratesLocalDataSourceNonFileImpl.saveRates(ratesUSDUSDEntity)
        assertFalse(result)
    }

    @Test
//    fun verifyLoadRatesFromNonFile() {
    fun `should return error after try to load RatesDataEntity from non-file`() {
        var ratesLocalDataSourceNonFileImpl = RatesLocalDataSourceImpl(
            fileInvalid, LocalToEntityMapper, EntityToLocalMapper
        )
        var ratesEntitiesLoaded = ratesLocalDataSourceNonFileImpl.loadRates()
        assertEquals(ratesEmptyEntity, ratesEntitiesLoaded)
    }

}