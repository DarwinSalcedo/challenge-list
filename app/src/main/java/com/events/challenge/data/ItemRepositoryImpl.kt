package com.events.challenge.data

import com.events.challenge.di.IoDispatcher
import com.events.challenge.domain.ItemModel
import com.events.challenge.domain.ItemRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.random.Random

class DefaultItemRepository @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ItemRepository {

    override suspend fun getItems(): Flow<Result<List<ItemModel>>> {
        delay(1500)
        return flow {
            emit(Result.success(generateDummyData()))

        }.flowOn(ioDispatcher)
            .catch {
                emit(Result.failure(it))
            }
    }

    override val number: Int
        get() =  Random.nextInt(60)

    private fun generateDummyData(): List<ItemModel> {
        val bombNumbers = listOf(1, 21, 23, 34, 10, 5, 50, 11, 25, 44, 18)
        if (number in bombNumbers) throw Exception("This number $number is a bombNumbers")
        val resultData = mutableListOf<ItemModel>()
        repeat(number) {
            resultData.add(ItemModel("Title #$it", "This is the description #$it"))
        }
        return resultData
    }
}
