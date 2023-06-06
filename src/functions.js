function getDailyCurrency() {
    var url = "https://www.cbr-xml-daily.ru/daily_utf8.xml";
    var response = $http.get(url);
    if (response && response.data && response.data.ValCurs && response.data.ValCurs.Valute) return response.data.ValCurs.Valute;
}

function getCurrencyValue(currency) {
    var dailyCurrency = getDailyCurrency();
    if (!dailyCurrency) return;
    return _.findWhere(dailyCurrency, {CharCode: currency});
}

function switchToOperator() {
    $.response.replies = $.response.replies || [];
    $.response.replies.push({
        type: "switch"
    });
}