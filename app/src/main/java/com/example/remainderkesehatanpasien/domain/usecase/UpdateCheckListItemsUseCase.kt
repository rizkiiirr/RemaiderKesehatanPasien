package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.CheckListItem
import com.example.remainderkesehatanpasien.domain.repository.CheckListRepository
import javax.inject.Inject

class UpdateCheckListItemsUseCase @Inject constructor(
    private val repository: CheckListRepository
) {
    suspend operator fun invoke(item: CheckListItem){
        repository.updateItem(item)
    }
}