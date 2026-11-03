package com.example.callfilter.adapter.output.db

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.callfilter.adapter.output.db.entity.BlockedContactEntity
import com.example.callfilter.adapter.output.db.migration.PreferencesToRoomMigrator
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
class PreferencesToRoomMigratorTest {

    private lateinit var context: Context
    private lateinit var database: AppDatabase
    private lateinit var prefs: SharedPreferences
    private lateinit var migrator: PreferencesToRoomMigrator

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        prefs = context.getSharedPreferences("call_filter_prefs", Context.MODE_PRIVATE)
        migrator = PreferencesToRoomMigrator(prefs, database.blockedContactDao())
    }

    @After
    fun tearDown() {
        database.close()
        prefs.edit().clear().commit()
    }

    @Test
    fun `deve migrar contatos bloqueados de SharedPreferences para Room e limpar preferencia antiga`() = runBlocking {
        // Given: Dados legados gravados no SharedPreferences
        val legacyNumbers = setOf("+5521999999999", "+5521888888888")
        prefs.edit().putStringSet("excluded_contacts", legacyNumbers).commit()

        // When: Executa o script de migração
        migrator.migrateIfNeeded()

        // Then: Os dados foram persistidos no Room
        val roomContacts = database.blockedContactDao().getAllBlockedContacts().first()
        assertEquals(2, roomContacts.size)
        val phoneNumbersInRoom = roomContacts.map { it.phoneNumber }.toSet()
        assertTrue(phoneNumbersInRoom.containsAll(legacyNumbers))

        // And: A chave do SharedPreferences foi removida após a migração
        val remainingPrefs = prefs.getStringSet("excluded_contacts", null)
        assertTrue(remainingPrefs == null)
    }

    @Test
    fun `nao deve falhar nem alterar Room se SharedPreferences estiver vazio`() = runBlocking {
        // Given: Nenhuma preferencia armazenada

        // When: Executa a migração
        migrator.migrateIfNeeded()

        // Then: Banco Room permanece vazio
        val roomContacts = database.blockedContactDao().getAllBlockedContacts().first()
        assertTrue(roomContacts.isEmpty())
    }
}