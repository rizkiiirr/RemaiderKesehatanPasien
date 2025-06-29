package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.CheckListItem
import com.example.remainderkesehatanpasien.domain.InvalidInputException
import com.example.remainderkesehatanpasien.domain.repository.CheckListRepository
import javax.inject.Inject

class AddCheckListItemsUseCase @Inject constructor(
    private val repository: CheckListRepository
) {
    @Throws(InvalidInputException::class)
    suspend operator fun invoke(title: String){
        if(title.isBlank()){
            throw InvalidInputException("Riwayat Tidak Boleh Kosong")
        }
        val newItem = CheckListItem(
            title = title,
            isChecked = false,
            timestamp = System.currentTimeMillis()
        )
        repository.insertItem(newItem)
    }
}