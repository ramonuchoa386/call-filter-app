package com.example.callfilter.adapter.output.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.callfilter.adapter.output.db.dao.CallLogDao
import com.example.callfilter.adapter.output.db.entity.CallLogEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CallLogDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: CallLogDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.callLogDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `deve registrar log de chamada e recuperar em ordem decrescente`() = runBlocking {
        val log1 = CallLogEntity(id = 1, phoneNumber = "+5521999999999", timestamp = 1000L, status = "BLOCKED")
        val log2 = CallLogEntity(id = 2, phoneNumber = "+5521888888888", timestamp = 2000L, status = "ACCEPTED")

        dao.insertCallLog(log1)
        dao.insertCallLog(log2)

        val logs = dao.getCallLogs().first()
        assertEquals(2, logs.size)
        assertEquals(2000L, logs[0].timestamp)
    }
}