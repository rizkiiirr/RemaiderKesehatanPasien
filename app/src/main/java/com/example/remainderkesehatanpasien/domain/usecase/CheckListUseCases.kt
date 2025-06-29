package com.example.remainderkesehatanpasien.domain.usecase

class CheckListUseCases(
    val getItems: GetCheckListItemsUseCase,
    val addItem: AddCheckListItemsUseCase,
    val updateItem: UpdateCheckListItemsUseCase,
    val deleteItem: DeleteCheckListItemsUseCase
)