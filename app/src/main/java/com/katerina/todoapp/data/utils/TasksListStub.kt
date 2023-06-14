package com.katerina.todoapp.data.utils

import com.katerina.todoapp.domain.models.TaskModel
import com.katerina.todoapp.domain.utils.TaskImportance

val tasksListStub = listOf(
    TaskModel(
        id = "1",
        text = "Купить продукты на неделю",
        importance = TaskImportance.HIGH,
        isDone = false,
        creationDateTimestamp = 1680796800,
        changeDateTimestamp = 1680796800
    ),
    TaskModel(
        id = "2",
        text = "Посетить занятия по йоге",
        importance = TaskImportance.NORMAL,
        isDone = true,
        creationDateTimestamp = 1680883200,
        changeDateTimestamp = 1680969600,
        deadlineDateTimestamp = 1681056000
    ),
    TaskModel(
        id = "3",
        text = "Забрать посылку на почте",
        importance = TaskImportance.HIGH,
        isDone = false,
        creationDateTimestamp = 1681142400,
        changeDateTimestamp = 1681228800
    ),
    TaskModel(
        id = "4",
        text = "Позвонить родным",
        importance = TaskImportance.LOW,
        isDone = false,
        creationDateTimestamp = 1681398000,
        changeDateTimestamp = 1681484400
    ),
    TaskModel(
        id = "5",
        text = "Сдать отчет о проделанной работе",
        importance = TaskImportance.HIGH,
        isDone = false,
        creationDateTimestamp = 1681571200,
        changeDateTimestamp = 1681657600
    ),
    TaskModel(
        id = "6",
        text = "Оплатить счета за коммунальные услуги",
        importance = TaskImportance.NORMAL,
        isDone = true,
        creationDateTimestamp = 1681740400,
        changeDateTimestamp = 1681826800,
        deadlineDateTimestamp = 1681913200
    ),
    TaskModel(
        id = "7",
        text = "Заказать билеты на концерт какого-то супер-крутого исполнителя, про которого говорят все друзья и знакомые. Он типа очень крутую музыку пишет и вообще все на этот концерт пойдут надо заценить",
        importance = TaskImportance.NORMAL,
        isDone = false,
        creationDateTimestamp = 1682009600,
        changeDateTimestamp = 1682096000
    ),
    TaskModel(
        id = "8",
        text = "Сходить на тренировку",
        importance = TaskImportance.LOW,
        isDone = true,
        creationDateTimestamp = 1682265200,
        changeDateTimestamp = 1682351600
    ),
    TaskModel(
        id = "9",
        text = "Заказать книги по чистой архитектуре",
        importance = TaskImportance.NORMAL,
        isDone = false,
        creationDateTimestamp = 1682438400,
        changeDateTimestamp = 1682524800
    ),
    TaskModel(
        id = "10",
        text = "Сходить к врачу (ФИО врача, номер кабинета)",
        importance = TaskImportance.HIGH,
        isDone = false,
        creationDateTimestamp = 1682607600,
        changeDateTimestamp = 1682694000,
        deadlineDateTimestamp = 1682780400
    ),
    TaskModel(
        id = "11",
        text = "Отправить фотографии с праздника бабушке и дедушке",
        importance = TaskImportance.NORMAL,
        isDone = true,
        creationDateTimestamp = 1682876800,
        changeDateTimestamp = 1682963200
    ),
    TaskModel(
        id = "12",
        text = "Отправить резюме на вакансию Junior Android-разработчик",
        importance = TaskImportance.HIGH,
        isDone = false,
        creationDateTimestamp = 1683132400,
        changeDateTimestamp = 1683218800
    ),
    TaskModel(
        id = "13",
        text = "Сделать необязательное домашнее задание по английскому языку",
        importance = TaskImportance.LOW,
        isDone = true,
        creationDateTimestamp = 1683305600,
        changeDateTimestamp = 1683392000
    ),
    TaskModel(
        id = "14",
        text = "Подготовиться к презентации",
        importance = TaskImportance.HIGH,
        isDone = true,
        creationDateTimestamp = 1683474800,
        changeDateTimestamp = 1683561200,
        deadlineDateTimestamp = 1683647600
    ),
    TaskModel(
        id = "15",
        text = "Сделать домашнюю работу #1 ШМР",
        importance = TaskImportance.HIGH,
        isDone = false,
        creationDateTimestamp = 1686562449,
        changeDateTimestamp = 1686562449,
        deadlineDateTimestamp = 1686978000
    )
)
