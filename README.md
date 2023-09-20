# reversi-deeplay
Reversi/Othello Game
Для запуска приложения выполните следующие действия:
1. В файле file.properties в модулe server укажите необходимый вам порт( по умолчанию 6070)
2. В файле file.properties в модуле client укажите необходимые вам
   ip адрес в поле host ( по умолчанию localhost)
   serverPort( по умолчанию 6070)
   botFarmPort( по умолчанию 6071)
   player( значение bot, если за вас будет играть bot; human, если будете играть самостоятельно)
   botName( ruslanBotMinMaxBlack, randomBotBlack, ruslanBotMinMaxWhite, randomBotWhite)
4. В файле file.properties в модуле botfarm укажите необходимый вам port( по умолчанию 6071)
5. Запустите Server.java в модуле server
6. Запустите BorFarm.java в модуле botfarm
   
Если вы хотите сыграть бот против бота, выполните следующие действия:
7. Запустите Client в модуле client ( важно в таком случае указать в файле file.properties в модуле client бота( поле botName), играющего за черный цвет, и запутсить именно этот клиент первым)
8. Запустите еще один Client в модуле client ( не забудьте указать в файле file.properties поле botName бота, играющего за белый цвет)
9. Игра начнется автоматически, по ее итогам будет выведен счет и победитель партии

