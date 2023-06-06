theme: /Currency
    
    state: Exchange
        q!: * ~курс [валют*] ($Currency/$ambiguousCurrency/валют*) *
        q: ($Currency/$ambiguousCurrency) || fromState = "/Currency/Exchange"
        script:
            $temp.searchCurrency = $parseTree._Currency;
        if: $parseTree._ambiguousCurrency && $parseTree._ambiguousCurrency.mostLikely
            script:
                var res = $nlp.matchPatterns($parseTree._ambiguousCurrency.mostLikely, ["$Currency"]);
                if (res && res.parseTree) $temp.searchCurrency = res.parseTree._Currency;
        if: $temp.searchCurrency
            script:
                $temp.currencyValue = getCurrencyValue($temp.searchCurrency.abbreviation);
            if: $temp.currencyValue
                a: На текущий момент 1{{$temp.searchCurrency.symbol}} составляет {{$temp.currencyValue.Value}} {{ $nlp.conform("рубль", $temp.currencyValue.Value) }}
            else:
                a: Нет данных по введенной валюте.
            go!: /AnyQuestions
        a: Укажите, пожалуйста, какая валюта Вас интересует?
