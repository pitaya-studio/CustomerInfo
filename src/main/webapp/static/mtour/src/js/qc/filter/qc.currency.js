/**
 * Created by ding on 2015/7/8.
 */

qc.filter('qcCurrency', ["$filter", function ($filter) {
    return function(amount, currencySymbol){

        var currency = $filter('currency');

        if(amount < 0){
            return currency(amount, currencySymbol).replace("(", "").replace(")", "").replace(currencySymbol,currencySymbol+'-');
        }

        return currency(amount, currencySymbol);
    };

}]);