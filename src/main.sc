require: currency.sc
require: functions.js
require: patterns.sc
  module = sys.zb-common
require: currency/currency.sc
  module = sys.zb-common
require: slotfilling/slotFilling.sc
  module = sys.zb-common

init:
    bind("preProcess", function($context) {
        if (!$jsapi.context().session.lastState) $reactions.answer("Добрый день!"); 
    });
    
    bind("postProcess", function($context) {
        if ($.currentState !== "/Reset") $jsapi.context().session.lastState = $.currentState;
        if (!_.contains(["/StopSession", "/SwitchToOperator"], $jsapi.context().session.lastState)) $reactions.timeout({interval: 180, targetState: "/StopSession"});
        log($jsapi.context().session);
    });
    
theme: /

    state: Start
        q!: $regex</start>
        q!: $hello
        a: Я Ваш персональный чатбот, чем могу помочь?

    state: AnyQuestions
        a: Остались ли у Вас еще вопросы?
        q: $no : endSession || toState = "/StopSession"
        
        state: Yes
            q: $yes
            a: Какой у вас вопрос?
        
    state: StopSession
        if: $parseTree._Root !== "endSession"
            a: Увы, я не дождался Вашего ответа.
        a: Всегда рад помочь. До свидания!
        script: $jsapi.stopSession();
    
    state: NoMatch || noContext = true
        event!: noMatch
        if: $session.noMatchCounter
            go!: /SwitchToOperator
        a: Извините, я не понимаю Ваш запрос. Пожалуйста, попробуйте перефразировать или задать другой вопрос.
        script: $session.noMatchCounter = 1;
            
    state: SwitchToOperator
        q!: * $switchToOperator *
        q!: * $obsceneWord *
        a: Соединяю с оператором.
        script: switchToOperator();
        
    state: Reset
        q!: reset
        a: Сброс сессионных данных
        script: $jsapi.stopSession();