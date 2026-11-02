package com.example.callfilter.adapter.output.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.callfilter.adapter.output.db.dao.BlockedContactDao
import com.example.callfilter.adapter.output.db.entity.BlockedContactEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BlockedContactDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: BlockedContactDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.blockedContactDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `deve inserir e buscar contatos bloqueados via Flow`() = runBlocking {
        val entity = BlockedContactEntity(
            phoneNumber = "+5521999999999",
            name = "Telemarketing",
            addedAt = System.currentTimeMillis()
        )

        dao.insertBlockedContact(entity)

        val list = dao.getAllBlockedContacts().first()
        assertEquals(1, list.size)
        assertEquals("+5521999999999", list[0].phoneNumber)
    }

    @Test
    fun `deve deletar contato bloqueado por numero de telefone`() = runBlocking {
        val entity = BlockedContactEntity(
            phoneNumber = "+5521888888888",
            name = null,
            addedAt = System.currentTimeMillis()
        )

        dao.insertBlockedContact(entity)
        dao.deleteBlockedContact("+5521888888888")

        val list = dao.getAllBlockedContacts().first()
        assertTrue(list.isEmpty())
    }
}