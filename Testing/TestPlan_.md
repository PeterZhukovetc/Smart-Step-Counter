### Содержание
  1. [Введение](#1)
  2. [Объект тестирования](#2)
  3. [Риски](#3)
  4. [Аспекты тестирования](#4)<br>
    4.1. [Возможность начать отслеживание местоположения](#001)<br>
    4.2. [Возможность добавить произвольную точку](#002)<br>
    4.3. [Возможность добавить точку на местоположении пользователя](#003)<br>
    4.4. [Возможность прекратить отслеживание местоположения](#004)<br>
    4.5. [Возможность обновления списка точек на карте](#005)<br>
5. [Подходы к тестированию](#5)
6. [Представление результатов](#6)
7. [Выводы](#7)

<a name="1"></a>
### 1. Введение
Этот план был разработан для тестирования приложения "NaviSport". 
Цель тестирования - проверка соответствия приложения требованиям, составленным при начале проектирования, проверка работоспособности приложения, а также его пригодности для эксплуатации.

<a name="2"></a>
### 2. Объект тестирования
Объект тестирования представляет Android приложение, использующее в работе Google карты, оно должно обладать следующими атрибутами качества:

  - выполнение поставленных задач в соответствии с требованиями к продукту;
  - эффективность использования ресурсов - приложение не должно вызывать зависаний устройства и занимать небольшой объем памяти;
  - стабильность - приложение не должно выдавать неожиданных и незапланированных ошибок, вызывающих авариное завершение программы;
  - сохранность данных(точек на карте) при закрытии приложения.

<a name="3"></a>
### 3. Риски
1. Запуск на устройствах со старыми версиями ОС Android.
2. Наличие возможности подключения устройства к сети Интернет.
3. Возможность использования геолокации в устройстве.

<a name="4"></a>
### 4. Аспекты тестирования
В ходе тестирования должна быть проверена реализация основных функций приложения, к которым относятся:

- Возможность начать отслеживание местоположения;
- Возможность добавить произвольную точку;
- Возможность добавить точку на местоположении пользователя;
- Возможность прекратить отслеживание местоположения;
- Возможность обновления списка точек на карте

### Функциональные требования:
<a name="001"></a>
#### Возможность начать отслеживание местоположения
Этот вариант использования небходимо протестировать на:
1. Реакцию приложения после нажатия кнопки "Get location", при выключенном отслеживании местоположения.
2. Начало отслеживания местоположения с последующим отображением на карте.

<a name="002"></a>
#### Возможность добавить произвольную точку
Этот вариант использования небходимо протестировать на:
1. Реакцию приложения после нажатия кнопки "Add new point" на отсутствие введенных данных.
2. Реакцию приложения после нажатия кнопки "Add new point" на заполненные поля.

<a name="003"></a>
#### Возможность добавить точку на местоположении пользователя
Этот вариант использования небходимо протестировать на:
1. Реакцию приложения на нажатие кнопки "Add new point" при отсутствие введенных данных.
2. Реакцию приложения на нажатие кнопки "Add new point" при заполненных полях и с отмеченным флажком.

<a name="004"></a>
#### Возможность прекратить отслеживание местоположения
Этот вариант использования небходимо протестировать на:
1. Реакцию приложения после нажатия кнопки "Get location", при включенном отслеживании местоположения.
2. Окончание отслеживания местоположения с последующим удалением маячка на карте.

<a name="005"></a>
#### Возможность обновления списка точек на карте
Этот вариант использования небходимо протестировать на:
1. Реакцию приложения после нажатия кнопки "Update points", при отсутствии точек.
2. Реакцию приложения после нажатия кнопки "Update points", при наличии точек.
3. Корректность отображения введенных точек на карте.

#### Нефункциональные требования:
- корректное поведение приложения при разной ориентации экрана;
- корректная функционирование приложения на протяжении всей сесси работы с приложением;

<a name="5"></a>
### 5. Подходы к тестированию
Проведение ручного тестирования.

<a name="6"></a>
### 6. Представление результатов
Результаты тестирования будут представлены в [таблице](https://github.com/PeterZhukovetc/Smart-Step-Counter/blob/master/Testing/TestResults_.md).

<a name="7"></a>
### 7. Выводы
В результате проведения тестирования удалось выяснить, что данный продукт выполняет все требования, которые были для него определены во время этапа проектирования. Однако, найденные "баги"  говорят о том, что продукт не готов к массовому распространению и использованию и разработчик должен их исправить.
