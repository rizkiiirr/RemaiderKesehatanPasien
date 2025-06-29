package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.CheckListItem
import com.example.remainderkesehatanpasien.domain.repository.CheckListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCheckListItemsUseCase @Inject constructor(
    private val repository: CheckListRepository
) {
    operator fun invoke(): Flow<List<CheckListItem>> {
        return repository.getAllItems()
    }
}