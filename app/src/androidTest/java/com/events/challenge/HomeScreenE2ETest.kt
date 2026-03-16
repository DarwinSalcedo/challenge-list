package com.events.challenge

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import com.events.challenge.di.RepositoryModule
import com.events.challenge.domain.ItemModel
import com.events.challenge.domain.ItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeE2ERepository @Inject constructor() : ItemRepository {
    var shouldFail = false
    override val number: Int get() = if (shouldFail) 10 else 2

    override suspend fun getItems(): Flow<Result<List<ItemModel>>> = flow {
        if (shouldFail) {
            emit(Result.failure(Exception("Fake Error")))
        } else {
            emit(
                Result.success(
                    listOf(
                        ItemModel("Title #0", "This is the description #0"),
                        ItemModel("Title #1", "This is the description #1")
                    )
                )
            )
        }
    }
}

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
class HomeScreenE2ETest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createEmptyComposeRule()

    @Inject
    lateinit var fakeRepository: FakeE2ERepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object TestRepositoryModule {
        @Provides
        @Singleton
        fun provideItemRepository(fakeE2ERepository: FakeE2ERepository): ItemRepository = fakeE2ERepository
    }

    @Test
    fun appLaunch_showsLoadingThenItemsAndHandlesClicks() {
        fakeRepository.shouldFail = false

        ActivityScenario.launch(MainActivity::class.java).use {
            composeTestRule.onNodeWithText("Random List Challenge").assertIsDisplayed()

            composeTestRule.waitUntil(timeoutMillis = 5000) {
                composeTestRule.onAllNodesWithText("Title #0").fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule.onNodeWithText("Title #0").assertIsDisplayed()
            composeTestRule.onNodeWithText("This is the description #0").assertIsDisplayed()
            composeTestRule.onNodeWithText("Title #1").assertIsDisplayed()

            composeTestRule.onNodeWithText("Title #0").performClick()
        }
    }

    @Test
    fun appLaunch_withBombNumber_showsErrorAndRecoversOnRetry() {
        fakeRepository.shouldFail = true

        ActivityScenario.launch(MainActivity::class.java).use {
            composeTestRule.waitUntil(timeoutMillis = 5000) {
                composeTestRule.onAllNodesWithText("Error: Fake Error").fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule.onNodeWithText("Error: Fake Error").assertIsDisplayed()
            composeTestRule.onNodeWithText("Retry").assertIsDisplayed()

            fakeRepository.shouldFail = false
            composeTestRule.onNodeWithText("Retry").performClick()
            composeTestRule.waitUntil(timeoutMillis = 5000) {
                composeTestRule.onAllNodesWithText("Title #0").fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule.onNodeWithText("Title #0").assertIsDisplayed()
            composeTestRule.onNodeWithText("Title #1").assertIsDisplayed()
        }
    }
}
